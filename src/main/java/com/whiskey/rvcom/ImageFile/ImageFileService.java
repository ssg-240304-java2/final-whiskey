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

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageFileService {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileService.class);
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "tif");

    @Autowired
    private ImageFileRepository imageFileRepository;

    /**
     * 이미지 파일을 업로드하고 해당 파일의 엔티티를 생성합니다.
     * 
     * @param filePath 업로드할 파일의 경로
     * @return 생성된 ImageFile 엔티티
     * @throws Exception 파일 업로드 실패 또는 유효하지 않은 파일 형식일 경우 발생
     */
    @Transactional
    public ImageFile uploadFile(String filePath) throws Exception {
        logger.info("파일 업로드 및 엔티티 생성 중: {}", filePath);

        if (!new File(filePath).exists()) {
            logger.error("파일이 존재하지 않습니다: {}", filePath);
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        if (!isImageFile(filePath)) {
            logger.error("유효하지 않은 이미지 파일 형식입니다: {}", filePath);
            throw new IllegalArgumentException("유효하지 않은 이미지 파일 형식입니다.");
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
     * 실제 파일 업로드를 수행합니다.
     * 
     * @param filePath 업로드할 파일의 경로
     * @return 업로드된 파일의 이름 정보를 담은 FileNameGroup 객체
     * @throws Exception 파일 업로드 실패 시 발생
     */
    private FileNameGroup uploadImageFile(String filePath) throws Exception {
        logger.info("이미지 파일 업로드 중: {}", filePath);

        FileUploader fileUploader = new FileUploader(filePath);
        return fileUploader.upload();
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
