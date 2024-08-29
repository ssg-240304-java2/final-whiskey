package com.whiskey.rvcom.controller;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

//    @GetMapping("/adminMain")
//    public String adminMain(Model model, HttpSession session) {
//        Member member = (Member) session.getAttribute("member");
//        if (member == null) {
//            return "redirect:/login";
//        }
//
//        String profileImageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/049/273/cover11.jpg";
//        if (member.getProfileImage() != null) {
//            profileImageUrl = ImagePathParser.parse(member.getProfileImage().getUuidFileName());
//        }
//
//        model.addAttribute("profileImageUrl", profileImageUrl);
//        model.addAttribute("memberName", member.getName());
//        return "adminMain";
//    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        // 대시보드 데이터를 모델에 추가
        return "admin/fragments/dashboard :: dashboard";
    }

//    @GetMapping("/user-management")
//    public String getUserManagement(Model model) {
//        // 사용자 관리 데이터를 모델에 추가
//        return "admin/fragments/user-management :: userManagement";
//    }

    @GetMapping("/report-management")
    public String getReportManagement(Model model) {
        // 신고 관리 데이터를 모델에 추가
        return "admin/fragments/report-management :: reportManagement";
    }

    @GetMapping("/statistics")
    public String getStatistics(Model model) {
        // 통계 데이터를 모델에 추가
        return "admin/fragments/statistics :: statistics";
    }

    @GetMapping("/restaurant-management")
    public String getRestaurantManagement(Model model) {
        return "admin/fragments/restaurant-management :: restaurantManagement";
    }
}
