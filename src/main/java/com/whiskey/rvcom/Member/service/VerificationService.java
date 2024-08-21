package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.Member.emailauth.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class VerificationService {

    private final RestTemplate restTemplate;
    private final EmailService emailService;

    @Autowired
    public VerificationService(RestTemplateBuilder builder, EmailService emailService) {
        this.restTemplate = builder.build();
        this.emailService = emailService;
    }

    public String generateAndSaveVerificationCode(String email) {
        String verificationCode = generateVerificationCode();
        String apiUrl = "${{secrets.MAIL_URL}}";

        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("code", verificationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                emailService.sendVerificationCode(email, verificationCode);
                return verificationCode;
            } else {
                throw new RuntimeException("Failed to save verification code. Response: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while saving verification code: " + e.getMessage(), e);
        }
    }

    public boolean verifyCode(String email, String code) {
        String apiUrl = "${{secrets.MAIL_URL}}" + email;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            return code.equals(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while verifying code: " + e.getMessage(), e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
