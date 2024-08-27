package com.whiskey.rvcom.ImageFile;

import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.repository.ImageFileRepository;
import com.whiskey.libs.file.FileUploader;
import com.whiskey.libs.file.FileNameGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageFileService {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileService.class);
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "tif");

    @Autowired
    private ImageFileRepository imageFileRepository;

    private final String host; // NCP 파일서버 주소(임의로 테스트 때문에 설정)

    public ImageFileService() {
        this.host = "http://web.dokalab.site:8083";
    }

    /**
     * 이미지 파일을 업로드하고 해당 파일의 엔티티를 생성합니다.
     * @param multipartFile 사용자가 업로드한 MultipartFile 객체
     * @return 생성된 ImageFile 엔티티
     * @throws Exception 파일 업로드 실패 또는 유효하지 않은 파일 형식일 경우 발생
     */
    @Transactional
    public ImageFile uploadFile(MultipartFile multipartFile) throws Exception {
        logger.info("파일 업로드 및 엔티티 생성 중: {}", multipartFile.getOriginalFilename());

        try {
            String localFilePath = saveFileLocally(multipartFile);

            if (!isImageFile(localFilePath)) {
                logger.error("유효하지 않은 이미지 파일 형식입니다: {}", localFilePath);
                deleteFileLocally(localFilePath);
                throw new IllegalArgumentException("유효하지 않은 이미지 파일 형식입니다.");
            }

            FileUploader fileUploader = new FileUploader(localFilePath, this.host);
            FileNameGroup uploadedFileName = fileUploader.upload();

            ImageFile imageFile = new ImageFile();
            imageFile.setOriginalFileName(uploadedFileName.getOriginalFileName());
            imageFile.setUuidFileName(uploadedFileName.getUuidFileName());

            deleteFileLocally(localFilePath);

            return imageFileRepository.save(imageFile);
        } catch (IOException e) {
            logger.error("파일 저장 중 IO 오류 발생", e);
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            logger.error("파일 업로드 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * MultipartFile을 웹서버 로컬에 저장합니다.
     * @param multipartFile 사용자가 업로드한 MultipartFile 객체
     * @return 저장된 로컬 파일 경로
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    private String saveFileLocally(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
        // 서버의 특정 디렉토리를 지정합니다. 예를 들어, "/app/multipartfiletest/"
        Path uploadDir = Paths.get("/app/multipartfiletest/");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path path = uploadDir.resolve(fileName);
        Files.write(path, multipartFile.getBytes());
        logger.info("파일을 서버 로컬에 저장했습니다: {}", path);
        return path.toString();
    }

    /**
     * 웹서버 로컬에 저장된 파일을 삭제합니다.
     * @param filePath 삭제할 파일 경로
     */
    private void deleteFileLocally(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            logger.info("로컬 파일을 삭제했습니다: {}", filePath);
        } catch (IOException e) {
            logger.error("로컬 파일 삭제 중 오류 발생: {}", filePath, e);
        }
    }

    /**
     * 주어진 파일이 허용된 이미지 파일 형식인지 확인합니다.
     *
     * @param filePath 확인할 파일의 경로
     * @return 허용된 이미지 파일 형식이면 true, 아니면 false
     */
    private boolean isImageFile(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}