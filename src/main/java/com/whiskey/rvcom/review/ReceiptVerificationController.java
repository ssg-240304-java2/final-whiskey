package com.whiskey.rvcom.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/receipt")
public class ReceiptVerificationController {
    @GetMapping("/receipt-verification")
    // 영수증 인증 페이지로 이동
    public String getReceiptVerification() {
        return "receiptVerification";
    }
}
