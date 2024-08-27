package com.whiskey.rvcom.Member.controller;

import com.whiskey.rvcom.Member.dto.VerifyPayload;
import com.whiskey.rvcom.Member.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailService emailService;

    @Autowired
    public EmailVerificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // 인증 코드 생성
        String verificationCode = generateVerificationCode();

        // 이메일 발송
        String subject = "이메일 인증 코드";
        String body = "요청하신 인증 코드는 " + verificationCode + " 입니다.";
        emailService.sendVerificationEmail(email, subject, body);

        // Redis에 인증 코드 저장
        emailService.saveVerificationCode(email, verificationCode);

        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyPayload payload) {
//        @RequestParam String email, @RequestParam String code

        System.out.println(payload.getEmail());
        System.out.println(payload.getCode());

        String email = payload.getEmail();
        String code = payload.getCode();

        try {
            String storedCode = emailService.getVerificationCode(email);

            if (storedCode != null && storedCode.equals(code)) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(false);
        }
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 899999) + 100000);
    }
}
