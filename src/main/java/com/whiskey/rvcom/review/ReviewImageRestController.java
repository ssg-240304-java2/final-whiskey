package com.whiskey.rvcom.review;

import com.whiskey.rvcom.ImageFile.ImageFileService;
import com.whiskey.rvcom.entity.resource.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ReviewImageRestController {

    private final ImageFileService imageFileService;

    @PostMapping("/review/uploadReviewImage")
    public Long uploadReviewImage(@RequestParam("file") MultipartFile file) {
        System.out.println("리뷰 이미지 업로드 요청");
        System.out.println("파일 이름: " + file.getOriginalFilename());
        // 리뷰 이미지 업로드
        if(file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        try {
            System.out.println("파일 업로드 중...");
            ImageFile uploadedImageFile = imageFileService.uploadFile(file);
            Long imageFileId = uploadedImageFile.getId();

            System.out.println("완료! 업로드된 이미지 파일 ID: " + imageFileId);
            return imageFileId;
        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 업로드 실패: " + e.getMessage());
        }
    }
}
