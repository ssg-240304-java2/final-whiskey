package com.whiskey.rvcom.util.fileupload;

import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@RestController
public class FileUploadController {
    // todo. 파일 업로드 컨트롤러 구현
//    String uploadDirectory = "uploads/";
//    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        final String UPLOAD_DIR = "uploads/";
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("/uploads/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드에 실패했습니다.");
        }
    }
}
