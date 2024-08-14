package com.whiskey.rvcom.ImageFile;

import com.whiskey.rvcom.entity.resource.ImageFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

@RestController
public class ImageFileController {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileController.class);

    @Autowired
    private ImageFileService imageFileService;

    /**
     * 이미지 파일을 업로드하는 엔드포인트입니다.
     * 
     * @param file 업로드할 MultipartFile 객체
     * @return 업로드 결과에 대한 ResponseEntity
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.info("파일 업로드 요청 받음: {}", file.getOriginalFilename());
        try {
            // 임시 파일 생성
            // 메모리에 있는 MultipartFile을 디스크의 파일로 변환하여 처리하기 위함
            File tempFile = File.createTempFile("upload-", "-temp");
            file.transferTo(tempFile);

            ImageFile imageFile = imageFileService.uploadFile(tempFile.getAbsolutePath());
            logger.info("파일 업로드 성공: {}", imageFile.getId());

            // 임시 파일 삭제
            // 업로드 작업이 완료된 후 불필요한 임시 파일을 제거하여 디스크 공간 확보
            Files.delete(tempFile.toPath());

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
