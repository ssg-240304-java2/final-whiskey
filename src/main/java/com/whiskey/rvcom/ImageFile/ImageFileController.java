package com.whiskey.rvcom.ImageFile;

import com.whiskey.rvcom.entity.resource.ImageFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageFileController {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileController.class);

    @Autowired
    private ImageFileService imageFileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("filePath") String filePath) {
        logger.info("파일 업로드 요청 받음: {}", filePath);
        try {
            ImageFile imageFile = imageFileService.createImageFileEntity(filePath);
            logger.info("파일 업로드 성공: {}", imageFile.getId());
            return ResponseEntity.ok(imageFile);
        } catch (IllegalArgumentException e) {
            logger.warn("파일 업로드를 위한 잘못된 입력: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("파일 업로드 중 오류 발생", e);
            return ResponseEntity.internalServerError().body("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}