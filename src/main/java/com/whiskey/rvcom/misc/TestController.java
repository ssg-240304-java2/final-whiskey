//package com.whiskey.rvcom.misc;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//
//@Controller
//public class TestController {
//
//    @GetMapping("/")
//    // 테스트 메인 페이지로 이동(로컬호스트 홈)
//    public String getMain() {
//        return "main";
//    }
//
//    @GetMapping("/mainPage")
//    // 메인 페이지로 이동
//    public String getMainPage() {
//        return "mainPage";
//    }
//
//    @GetMapping("/login")
//    // 로그인 페이지로 이동
//    public String getLogin() {
//        return "login";
//    }
//
//    @GetMapping("/register")
//    // 회원가입 페이지로 이동
//    public String getRegister() {
//        return "register";
//    }
//
//    @GetMapping("/mypage")
//    // 마이페이지로 이동
//    public String getMypage() {
//        return "mypage";
//    }
//
//    @GetMapping("/restaurant")
//    // 레스토랑 상세 페이지로 이동
//    public String getRestaurantDetail() {
//        return "restaurantDetail";
//    }
//
//    @GetMapping("/receipt-verification")
//    // 영수증 인증 페이지로 이동
//    public String getReceiptVerification() {
//        return "receiptVerification";
//    }
//
//    @GetMapping("/write-review")
//    // 리뷰 작성 페이지로 이동
//    public String getWriteReview() {
//        return "writeReview";
//    }
//
//    @GetMapping("/search-results")
//    // 검색 결과 페이지로 이동
//    public String searchResults() {
//        return "searchResults";
//    }
//
//    @GetMapping("/register-store")
//    // 매장 등록 페이지로 이동
//    public String getRegisterStore() {
//        return "register-store";
//    }
//
//
//}
