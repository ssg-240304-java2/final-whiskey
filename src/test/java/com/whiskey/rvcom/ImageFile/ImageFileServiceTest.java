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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

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

    private final String filePath = "C:\\Users\\choo\\Desktop\\racoon-pedro.gif";
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
     * MultipartFile을 사용하여 파일을 업로드하고, 결과를 검증합니다.
     */
    @Test
    public void testFileUpload() throws Exception {
        logger.info("실제 NCP 서버 파일 업로드 테스트 시작: {}", filePath);

        // 테스트용 MultipartFile 생성
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/gif", input);

        // FileUploader를 생성할 때 host 변수 사용
        FileUploader fileUploader = new FileUploader(filePath, host);

        // 파일 업로드 실행
        FileNameGroup uploadedFileName = fileUploader.upload();

        assertNotNull(uploadedFileName);
        assertNotNull(uploadedFileName.getOriginalFileName());
        assertNotNull(uploadedFileName.getUuidFileName());
        assertTrue(uploadedFileName.getOriginalFileName().endsWith("racoon-pedro.gif"));
        logger.info("실제 NCP 서버 파일 업로드 성공: {} -> {}", uploadedFileName.getOriginalFileName(), uploadedFileName.getUuidFileName());
    }

    /**
     * 실제 NCP 서버에 파일을 업로드하는 기능을 테스트합니다.
     * 모의 객체가 아닌 실제 MultipartFile을 사용하여 파일을 업로드하고, 결과를 검증합니다.
     */
    @Test
    public void testRealFileUpload() throws Exception {
        logger.info("실제 NCP 서버 파일 업로드 테스트 시작: {}", filePath);

        // 실제 파일로 MultipartFile 생성
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return "image/gif";
            }

            @Override
            public boolean isEmpty() {
                return file.length() == 0;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return Files.readAllBytes(file.toPath());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(file);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.copy(file.toPath(), dest.toPath());
            }
        };
        logger.info("MultipartFile 생성 완료: 원본 파일 이름 = {}, 크기 = {} bytes", multipartFile.getOriginalFilename(), multipartFile.getSize());

        // ImageFileService를 사용하여 파일 업로드(현재 이 테스트 코드에서는 작동이 안됨.)
        logger.info("파일 업로드 시작: {}", multipartFile.getOriginalFilename());
        ImageFile result = imageFileService.uploadFile(multipartFile);
        logger.info("파일 업로드 완료: {}", result);

        assertNotNull(result);
        logger.info("결과 객체가 null이 아님을 확인");

        assertNotNull(result.getOriginalFileName());
        logger.info("원본 파일 이름이 null이 아님을 확인");

        assertNotNull(result.getUuidFileName());
        logger.info("UUID 파일 이름이 null이 아님을 확인");

        assertTrue(result.getOriginalFileName().endsWith("racoon-pedro.gif"));
        logger.info("원본 파일 이름이 'racoon-pedro.gif'로 끝남을 확인");

        logger.info("ImageFileService를 사용한 실제 NCP 서버 파일 업로드 성공: {} -> {}", result.getOriginalFileName(), result.getUuidFileName());
    }

    /**
     * 유효하지 않은 파일 확장자를 가진 파일을 업로드하려 할 때 예외가 발생하는지 테스트합니다.
     * IllegalArgumentException이 발생하고, 적절한 에러 메시지가 포함되어 있는지 확인합니다.
     */
    @Test
    public void testInvalidFileExtension() throws IOException {
        String invalidFilePath = "C:\\Users\\choo\\Desktop\\invalid.txt";
        logger.info("유효하지 않은 파일 확장자 테스트 시작: {}", invalidFilePath);

        File file = new File(invalidFilePath);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", input);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            imageFileService.uploadFile(multipartFile);
        });

        assertEquals("유효하지 않은 이미지 파일 형식입니다.", exception.getMessage());
        logger.info("유효하지 않은 파일 확장자 테스트 성공"); // 테스트 작동 확인
    }

    /**
     * 존재하지 않는 파일을 업로드하려 할 때 예외가 발생하는지 테스트합니다.
     * IOException이 발생하는지 확인합니다.
     */
    @Test
    public void testNonExistentFile() {
        String nonExistentFilePath = "C:\\Users\\choo\\Desktop\\non-existent.jpg";
        logger.info("존재하지 않는 파일 테스트 시작: {}", nonExistentFilePath);

        MultipartFile multipartFile = new MockMultipartFile("file",
                "non-existent.jpg", "image/jpeg", new byte[0]);

        assertThrows(IOException.class, () -> {
            imageFileService.uploadFile(multipartFile);
        });

        logger.info("존재하지 않는 파일 테스트 성공"); // 테스트 작동 확인
    }
}