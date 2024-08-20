package com.whiskey.rvcom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OwnerFragmentController {

    @GetMapping("/fragments/owner/{fragment}")
    public String getOwnerFragment(@PathVariable String fragment) {
        return "fragments/owner/" + fragment;
    }
}
