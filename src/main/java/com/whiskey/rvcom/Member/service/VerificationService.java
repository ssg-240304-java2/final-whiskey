package com.whiskey.rvcom.Member.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class VerificationService {

    private final RestTemplate restTemplate;

    @Value("${{secret_verification_api_url}}")
    private String apiUrl;

    public VerificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 인증 코드 생성 및 저장
    public String generateAndSaveVerificationCode(String email) {
        String verificationCode = generateRandomCode(); // 난수 생성
        String url = apiUrl + "?key=" + email + "&value=" + verificationCode;

        // 3분간 유효한 코드 저장 요청
        restTemplate.postForObject(url, null, Void.class);

        return verificationCode;
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String inputCode) {
        String url = apiUrl + "?key=" + email;
        String storedCode = restTemplate.getForObject(url, String.class);

        return storedCode != null && storedCode.equals(inputCode);
    }

    // 난수 생성
    private String generateRandomCode() {
        int codeLength = 6; // 예: 6자리 코드
        Random random = new Random();
        StringBuilder code = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10)); // 0-9 사이의 숫자 난수 생성
        }

        return code.toString();
    }
}
