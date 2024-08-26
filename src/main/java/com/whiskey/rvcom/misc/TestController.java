package com.whiskey.rvcom.misc;

import com.whiskey.rvcom.entity.restaurant.RestaurantCategory;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TestController {

    RestaurantService restaurantService;

    public TestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/test")
    // 테스트 메인 페이지로 이동(로컬호스트 홈)
    public String getMain() {
        return "main";
    }

    @GetMapping("/")
    // 메인 페이지로 이동
    public String getMainPage(Model model) {
        model.addAttribute("categoryList", RestaurantCategory.values());
        return "mainPage";
    }

    @GetMapping("/login")
    // 로그인 페이지로 이동
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    // 회원가입 페이지로 이동
    public String getRegister() {
        return "register";
    }

    @GetMapping("/mypage")
    // 마이페이지로 이동
    public String getMypage() {
        return "mypage";
    }

    @GetMapping("/restaurant")
    // 레스토랑 상세 페이지로 이동
    public String getRestaurantDetail() {
        return "restaurantDetail";
    }

//    @GetMapping("/receipt-verification")
//    // 영수증 인증 페이지로 이동
//    public String getReceiptVerification() {
//        return "receiptVerification";
//    }

    @GetMapping("/write-review")
    // 리뷰 작성 페이지로 이동
    public String getWriteReview() {
        return "writeReview";
    }

    @GetMapping("/register-store")
    // 매장 등록 페이지로 이동
    public String getRegisterStore() {
        return "register-store";
    }

    @GetMapping("/owner-dashboard")
    // 점주 관리자 대시보드 페이지로 이동
    public String getOwnerDashboard() {
        return "owner/dashboard";
    }

    @GetMapping("/adminMain")
    // 관리자-신고 페이지로 이동
    public String getAdminReport() {
        return "admin/adminMain";
    }

    @GetMapping("/adminReport")
    public void adminReport() {
    }
}
