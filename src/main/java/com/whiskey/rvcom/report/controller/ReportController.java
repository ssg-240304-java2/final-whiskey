package com.whiskey.rvcom.report.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/restaurantreport")
    public String getRestaurantReport() {
        return "report/restaurantreport";
    }

    @GetMapping("/restaurantdetail")
    public String getRestaurantDetail() {
        return "report/restaurantdetail";
    }

    @GetMapping("/reviewreport")
    public String getReviewReport() {
        return "report/reviewreport";
    }

    @GetMapping("/reviewdetail")
    public String getReviewDetail() {
        return "report/reviewdetail";
    }

    @GetMapping("/reviewcommentreport")
    public String getReviewCommentReport() {
        return "report/reviewcommentreport";
    }

    @GetMapping("/reviewcommentdetail")
    public String getReviewCommentDetail() {
        return "report/reviewcommentdetail";
    }


    @PostMapping("/valid")
    @ResponseBody
    public Map<String, Object> checkLogin(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Object member = session.getAttribute("member");

        if (member == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
        } else {
            response.put("success", true);
        }

        return response;
    }
}
