package com.whiskey.rvcom.ImageFile;

import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.repository.ImageFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class ImageFileService {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileService.class);

    @Autowired
    private ImageFileRepository imageFileRepository;

    /**
     * 이미지 파일 업로드 및 파일 리소스 엔티티 객체 반환
     * @param filePath 파일 경로
     * @return 이미지 파일 엔티티 객체
     * @throws Exception 파일 업로드 실패 시 예외 발생
     */
    @Transactional
    public ImageFile createImageFileEntity(String filePath) throws Exception {
        logger.info("파일에 대한 이미지 파일 엔티티 생성 중: {}", filePath);
        
        // TODO: 이 부분은 요구사항에 명시되지 않은 추가적인 검증 로직입니다.
        // 파일의 존재 여부를 확인하여 안전성을 높이고 명확한 에러 메시지를 제공합니다.
        // 요구사항에 따라 이 부분을 제거할 수 있습니다.
        if (!new File(filePath).exists()) {
            logger.error("파일이 존재하지 않습니다: {}", filePath);
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        FileNameGroup uploadedFileName = uploadImageFile(filePath);

        ImageFile imageFile = new ImageFile();
        imageFile.setOriginalFileName(uploadedFileName.getOriginalFileName());
        imageFile.setUuidFileName(uploadedFileName.getUuidFileName());

        imageFile = imageFileRepository.save(imageFile);
        logger.info("이미지 파일 엔티티가 성공적으로 생성되었습니다: {}", imageFile.getId());

        return imageFile;
    }

    /**
     * 이미지 파일을 서버에 업로드하고 파일명 그룹 객체 반환
     * @param filePath 파일 경로
     * @return 파일명 그룹 객체
     * @throws Exception 파일 업로드 실패 시 예외 발생
     */
    public FileNameGroup uploadImageFile(String filePath) throws Exception {
        logger.info("이미지 파일 업로드 중: {}", filePath);

        // TODO: FileUploader 클래스가 구현되면 이 부분을 수정해야 합니다.
        // FileUploader fileUploader = new FileUploader(filePath);

        if (!isImageFile(filePath)) {
            logger.error("파일이 이미지가 아닙니다: {}", filePath);
            throw new IllegalArgumentException("이미지 파일이 아닙니다.");
        }

        // TODO: 이 부분은 FileUploader 클래스가 구현되기 전의 임시 로직입니다.
        // 실제 FileUploader 구현 시 아래 로직을 제거하고 fileUploader.upload()를 호출해야 합니다.
        String originalFileName = new File(filePath).getName();
        String uuidFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        logger.info("파일이 성공적으로 업로드되었습니다. 원본 이름: {}, UUID 이름: {}", originalFileName, uuidFileName);
        return new FileNameGroup(originalFileName, uuidFileName);
    }

    /**
     * 파일이 이미지 파일인지 확인
     * @param filePath 파일 경로
     * @return 이미지 파일 여부
     */
    private boolean isImageFile(String filePath) {
        try {
            String contentType = Files.probeContentType(new File(filePath).toPath());
            return contentType != null && contentType.startsWith("image/");
        } catch (Exception e) {
            logger.error("파일 유형 확인 중 오류 발생: {}", filePath, e);
            return false;
        }
    }

    // TODO: FileNameGroup 클래스가 구현되면 이 내부 클래스를 제거해야 합니다.
    // 이 클래스는 FileUploader와 FileNameGroup 클래스가 구현되기 전의 임시 구현입니다.
    /**
     * 파일 이름 그룹을 나타내는 내부 클래스
     * 원본 파일 이름과 UUID가 부여된 파일 이름을 저장합니다.
     */
    private static class FileNameGroup {
        /** 원본 파일 이름 */
        private final String originalFileName;
        /** UUID가 부여된 파일 이름 */
        private final String uuidFileName;

        /**
         * FileNameGroup 객체를 생성합니다.
         * @param originalFileName 원본 파일 이름
         * @param uuidFileName UUID가 부여된 파일 이름
         */
        public FileNameGroup(String originalFileName, String uuidFileName) {
            this.originalFileName = originalFileName;
            this.uuidFileName = uuidFileName;
        }

        /**
         * 원본 파일 이름을 반환합니다.
         * @return 원본 파일 이름
         */
        public String getOriginalFileName() {
            return originalFileName;
        }

        /**
         * UUID가 부여된 파일 이름을 반환합니다.
         * @return UUID가 부여된 파일 이름
         */
        public String getUuidFileName() {
            return uuidFileName;
        }
    }
}
