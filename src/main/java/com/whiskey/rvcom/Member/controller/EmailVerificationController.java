package com.whiskey.rvcom.Member.controller;

import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.Member.dto.ResponseModel;
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
        String body = "만나서 반갑습니다!! FoodPolio 입니다.\n 회원가입에서 이 인증코드를 " + verificationCode + " 입력해주세용 :)";
        emailService.sendVerificationEmail(email, subject, body);

        // Redis에 인증 코드 저장
        emailService.saveVerificationCode(email, verificationCode);

        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestBody VerifyPayload responseModel) {
//        System.out.println(responseModel);
//        System.out.println(responseModel.getEmail());

        String storedCode = emailService.getVerificationCode(responseModel.getEmail());
        System.out.println("storedCode = " + storedCode);

        return storedCode;
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 899999) + 100000);
    }
}
