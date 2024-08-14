package com.whiskey.rvcom.ImageFile;

import com.whiskey.libs.file.FileNameGroup;
import com.whiskey.rvcom.entity.resource.ImageFile;
import com.whiskey.rvcom.repository.ImageFileRepository;
import com.whiskey.libs.file.FileUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * ImageFileService 클래스의 테스트를 위한 클래스입니다.
 * 이 클래스는 이미지 파일 업로드 및 관련 예외 처리를 테스트합니다.
 */
@SpringBootTest
public class ImageFileServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ImageFileServiceTest.class);

    @Autowired
    private ImageFileService imageFileService;

    @MockBean
    private ImageFileRepository imageFileRepository;

    private final String filePath = "C:\\Users\\kebin\\Desktop\\racoon-pedro.gif";
    private final String host = "http://web.dokalab.site:8083";

    /**
     * 각 테스트 메소드 실행 전에 실행되는 설정 메소드입니다.
     * ImageFileRepository의 save 메소드를 모킹하여 항상 ID가 1L인 ImageFile 객체를 반환하도록 설정합니다.
     */
    @BeforeEach
    public void setup() {
        when(imageFileRepository.save(any(ImageFile.class))).thenAnswer(invocation -> {
            ImageFile savedFile = invocation.getArgument(0);
            savedFile.setId(1L);
            return savedFile;
        });
    }

    /**
     * 실제 NCP 서버에 파일을 업로드하는 기능을 테스트합니다.
     * FileUploader를 사용하여 파일을 업로드하고, 결과를 검증합니다.
     */
    @Test
    public void testFileUpload() throws Exception {
        logger.info("실제 NCP 서버 파일 업로드 테스트 시작: {}", filePath);

        FileUploader realFileUploader = new FileUploader(filePath, host);
        FileNameGroup result = realFileUploader.upload();

        assertNotNull(result);
        assertNotNull(result.getOriginalFileName());
        assertNotNull(result.getUuidFileName());
        assertTrue(result.getOriginalFileName().endsWith("racoon-pedro.gif"));
        logger.info("실제 NCP 서버 파일 업로드 성공: {} -> {}", result.getOriginalFileName(), result.getUuidFileName());
    }
        
    /**
     * 유효하지 않은 파일 확장자를 가진 파일을 업로드하려 할 때 예외가 발생하는지 테스트합니다.
     * IllegalArgumentException이 발생하고, 적절한 에러 메시지가 포함되어 있는지 확인합니다.
     */
    @Test
    public void testInvalidFileExtension() {
        String invalidFilePath = "C:\\Users\\kebin\\Desktop\\invalid.txt";
        logger.info("유효하지 않은 파일 확장자 테스트 시작: {}", invalidFilePath);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imageFileService.uploadFile(invalidFilePath);
        });

        assertEquals("유효하지 않은 이미지 파일 형식입니다.", exception.getMessage());
        logger.info("유효하지 않은 파일 확장자 테스트 성공");
    }

    /**
     * 존재하지 않는 파일을 업로드하려 할 때 예외가 발생하는지 테스트합니다.
     * IllegalArgumentException이 발생하고, 적절한 에러 메시지가 포함되어 있는지 확인합니다.
     */
    @Test
    public void testNonExistentFile() {
        String nonExistentFilePath = "C:\\Users\\kebin\\Desktop\\non-existent.jpg";
        logger.info("존재하지 않는 파일 테스트 시작: {}", nonExistentFilePath);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imageFileService.uploadFile(nonExistentFilePath);
        });

        assertEquals("파일이 존재하지 않습니다.", exception.getMessage());
        logger.info("존재하지 않는 파일 테스트 성공");
    }
}
