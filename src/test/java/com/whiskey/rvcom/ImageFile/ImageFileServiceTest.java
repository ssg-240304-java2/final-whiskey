package com.whiskey.rvcom.ImageFile;

import com.whiskey.rvcom.entity.resource.ImageFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ImageFileServiceTest {

    @Autowired
    private ImageFileService imageFileService;

    @TempDir
    Path tempDir;

    @Test
    public void testCreateImageFileEntity() throws Exception {
        // 테스트용 이미지 파일 생성
        Path imagePath = tempDir.resolve("test_image.jpg");
        Files.write(imagePath, "fake image content".getBytes());

        // 서비스 메소드 호출
        ImageFile imageFile = imageFileService.createImageFileEntity(imagePath.toString());

        // 검증
        assertNotNull(imageFile);
        assertNotNull(imageFile.getId());
        assertEquals("test_image.jpg", imageFile.getOriginalFileName());
        assertTrue(imageFile.getUuidFileName().endsWith("_test_image.jpg"));
        assertNotNull(imageFile.getUploadedAt());
    }

    @Test
    public void testCreateImageFileEntityWithNonImageFile() throws IOException {
        // 테스트용 비이미지 파일 생성
        Path textPath = tempDir.resolve("test_file.txt");
        Files.write(textPath, "This is not an image".getBytes());

        // 예외 발생 검증
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imageFileService.createImageFileEntity(textPath.toString());
        });

        assertEquals("이미지 파일이 아닙니다.", exception.getMessage());
    }

    @Test
    public void testCreateImageFileEntityWithNonExistentFile() {
        // 존재하지 않는 파일 경로
        String nonExistentPath = tempDir.resolve("non_existent.jpg").toString();

        // 예외 발생 검증
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imageFileService.createImageFileEntity(nonExistentPath);
        });

        assertEquals("파일이 존재하지 않습니다.", exception.getMessage());
    }
}
