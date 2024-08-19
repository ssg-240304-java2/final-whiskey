package com.whiskey.rvcom.report.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/restaurantReport")
    public String getRestaurantReport() {
        return "report/restaurantReport";
    }

    @GetMapping("/restaurantDetail")
    public String getRestaurantDetail() {
        // restaurantDetail.html 파일을 templates/report 폴더 내에서 찾음
        return "report/restaurantDetail"; // view name
    }
}
