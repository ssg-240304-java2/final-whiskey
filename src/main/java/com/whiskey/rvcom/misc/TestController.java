package com.whiskey.rvcom.misc;

import com.whiskey.rvcom.entity.restaurant.OpenCloseTime;
import com.whiskey.rvcom.entity.restaurant.Restaurant;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@Slf4j
public class TestController {

    RestaurantService restaurantService;

    public TestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/")
    // 테스트 메인 페이지로 이동(로컬호스트 홈)
    public String getMain() {
        return "main";
    }

    @GetMapping("/mainPage")
    // 메인 페이지로 이동
    public String getMainPage() {
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

    @GetMapping("/search-results")
    // 검색 결과 페이지로 이동
    public String searchResults() {
        return "searchResults";
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
        return "adminMain";
    }


    @GetMapping("/adminReport")
    public void adminReport() {
    }

    @GetMapping("/restaurant/{restaurantId}/{tab}")
    public String getRestaurantDetailWithTab(Model model, @PathVariable Long restaurantId, @PathVariable String tab) {
        // TODO: restaurantId와 tab에 따른 데이터 로딩 로직 구현
        // TODO: 모델에 restaurantId와 tab 정보 추가
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        model.addAttribute("restaurant", restaurant);

        String today = LocalDate.now().getDayOfWeek().toString();

        switch (today) {
            case "MONDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getMonday(), model);
                break;
            case "TUESDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getTuesday(), model);
                break;
            case "WEDNESDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getWednesday(), model);
                break;
            case "THURSDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getThursday(), model);
                break;
            case "FRIDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getFriday(), model);
                break;
            case "SATURDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getSaturday(), model);
                break;
            case "SUNDAY":
                restaurantTimeData(restaurant.getWeeklyOpenCloseTime().getSunday(), model);
                break;
        }
        return "restaurantDetail";

    }

    private void restaurantTimeData(OpenCloseTime openCloseTime, Model model) {
        String hours;
        if (openCloseTime == null) {
            model.addAttribute("status", "미영업");
            hours = "";
        } else if (openCloseTime.isOpen()) {
            String openTime = openCloseTime.getOpenTime();
            String closeTime = openCloseTime.getCloseTime();
            LocalTime open = LocalTime.parse(openTime);
            LocalTime close = LocalTime.parse(closeTime);
            LocalTime now = LocalTime.now();
            if (now.isBefore(open) || now.isAfter(close)) {
                model.addAttribute("status", "영업종료");
            } else {
                model.addAttribute("status", "영업중");
            }
            hours = openCloseTime.getOpenTime() + " - " + openCloseTime.getCloseTime();
        } else {
            model.addAttribute("status", "미영업");
            hours = "";
        }
        model.addAttribute("hours", hours);
    }
}
