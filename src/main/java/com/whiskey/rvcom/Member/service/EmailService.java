package com.whiskey.rvcom.Member.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whiskey.rvcom.Member.dto.EmailRequest;
import com.whiskey.libs.rest.request.RequestMethod;
import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.Member.dto.RedisRequestPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    public void sendVerificationEmail(String to, String subject, String text) {
        var invoker = RestInvoker.create("http://web.dokalab.site:8081/api/email/send", null);

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(to);
        emailRequest.setSubject(subject);
        emailRequest.setText(text);

        try {
            // 요청을 보냅니다.
            try {
                invoker.request(emailRequest, EmailRequest.class, RequestMethod.POST);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occured");
            }
//            log.info("Raw response from server: {}", response);

            // 응답이 올바른 JSON인지 확인하고 파싱합니다.
//            if (response != null && response.trim().startsWith("{")) {
//                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
//
//                if (jsonResponse != null && jsonResponse.has("status") && "success".equals(jsonResponse.get("status").getAsString())) {
//                    log.info("Email sent successfully.");
//                } else {
//                    log.error("Failed to send email. Response: {}", response);
//                    throw new RuntimeException("Failed to send email.");
//                }
//            } else {
//                log.error("Invalid JSON response: {}", response);
//                throw new RuntimeException("Invalid JSON response: " + response);
//            }

        } catch (Exception e) {
            log.error("Exception occurred while sending verification email", e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void saveVerificationCode(String email, String code) {
        try {
            String apiUrl = "http://web.dokalab.site:8084/api/redis/save";

            RedisRequestPayload payload = new RedisRequestPayload();
            payload.setKey(email);
            payload.setValue(code);

            RestInvoker<String> invoker = RestInvoker.create(apiUrl, null);
            invoker.request(payload, RedisRequestPayload.class, RequestMethod.POST);

            System.out.println("레디스 저장 완료");
        } catch (Exception e) {
            log.error("Redis 저장 중 오류가 발생했습니다.", e);
            throw new RuntimeException("Redis 저장 중 오류가 발생했습니다.", e);
        }
    }

    public String getVerificationCode(String email) {
        try {
            String apiUrl = String.format("http://web.dokalab.site:8084/api/redis/get?key=%s", email);
            System.out.println("email = " + email);

            RestInvoker<String> invoker = RestInvoker.create(apiUrl, String.class);
            return invoker.request(RequestMethod.GET);
        } catch (Exception e) {
            log.error("Redis에서 코드 조회 중 오류가 발생했습니다.", e);
            throw new RuntimeException("Redis에서 코드 조회 중 오류가 발생했습니다.", e);
        }
    }
}

