package com.whiskey.rvcom.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping("/restaurant")
    public String getRestaurantReport() {
        return "report/restaurantReport";
    }
}
