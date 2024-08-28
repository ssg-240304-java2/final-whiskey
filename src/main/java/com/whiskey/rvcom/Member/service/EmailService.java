package com.whiskey.rvcom.Member.service;

import com.whiskey.rvcom.Member.dto.EmailRequest;
import com.whiskey.libs.rest.request.RequestMethod;
import com.whiskey.libs.rest.request.RestInvoker;
import com.whiskey.rvcom.Member.dto.RedisRequestPayload;
import com.whiskey.rvcom.Member.dto.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
            invoker.request(emailRequest, EmailRequest.class, RequestMethod.POST);
            System.out.println("이메일을 성공적으로 보냈습니다");
        } catch (NullPointerException e) {
            System.out.println("이메일을 성공적으로 보냈습니다");
        } catch (IOException e) {
            System.out.println("DB서버에 문제가 있습니다. DB서버를 확인해주세요");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("이메일을 보내는 문제가 생겼습니다.");
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

            // RestInvoker 인스턴스를 생성하고 GET 요청을 수행하여 문자열 응답을 받습니다.
            RestInvoker<ResponseModel> invoker = RestInvoker.create(apiUrl, ResponseModel.class);
            ResponseModel request = invoker.request(RequestMethod.GET);

            return request.getKey();

        } catch (Exception e) {
            System.err.println("Redis에서 코드 조회 중 오류가 발생했습니다.");
            throw new RuntimeException("Redis에서 코드 조회 중 오류가 발생했습니다.", e);
        }
    }
}

