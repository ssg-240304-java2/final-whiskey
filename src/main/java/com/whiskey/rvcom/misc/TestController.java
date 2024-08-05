package com.whiskey.rvcom.misc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TestController {

    @GetMapping("/")
    public String getMain() {
        return "main";
    }

    @GetMapping("/mainPage")
    public String getMainPage() {
        return "mainPage";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @GetMapping("/mypage")
    public String getMypage() {
        return "mypage";
    }

    @GetMapping("/restaurant")
    public String getRestaurantDetail() {
        return "restaurantDetail";
    }

    @GetMapping("/receipt-verification")
    public String getReceiptVerification() {
        return "receiptVerification";
    }
    
    @GetMapping("/write-review")
    public String getWriteReview() {
        return "writeReview";
    }
    
    @GetMapping("/search-results")
        public String searchResults() {
            return "searchResults";
    }

}
