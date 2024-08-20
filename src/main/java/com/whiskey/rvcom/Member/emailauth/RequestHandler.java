package com.whiskey.rvcom.Member.emailauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class RequestHandler {
//    private final EmailService emailService;
//
//    @Autowired
//    public RequestHandler(EmailService emailService) {
//        this.emailService = emailService;
//    }

    // todo. change params to a json object
//    @PostMapping("/send")
//    public String sendEmail(@RequestBody RequestObject req) {
//        emailService.sendSimpleMessage(req.getTo(), req.getSubject(), req.getText());
//        return "Email sent successfully";
//    }

    private final JavaMailSender mailSender;

    @Autowired
    public RequestHandler(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody RequestObject mailRequestObject) {
        try {
            System.out.println("Trying to send mail");
            System.out.println("To: " + mailRequestObject.getTo());
            System.out.println("Subject: " + mailRequestObject.getSubject());
            System.out.println("Text: " + mailRequestObject.getText());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("${{secrets.MAIL_URL}}");
            message.setTo(mailRequestObject.getTo());
            message.setSubject(mailRequestObject.getSubject());
            message.setText(mailRequestObject.getText());
            mailSender.send(message);
            return ResponseEntity.ok("Mail sent successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send mail");
        }
    }
}