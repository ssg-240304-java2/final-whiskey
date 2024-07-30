package com.whiskey.rvcom.misc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String getMain() {
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

    @GetMapping("/restaurant")
    public String getRestaurantDetail() {
        return "restaurantDetail";
    }
}
