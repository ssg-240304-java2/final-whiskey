package com.whiskey.rvcom.controller;

import com.whiskey.rvcom.entity.member.Member;
import com.whiskey.rvcom.restaurant.service.RestaurantService;
import com.whiskey.rvcom.util.ImagePathParser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OwnerFragmentController {

    private final RestaurantService restaurantService;

    public OwnerFragmentController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/fragments/owner/{fragment}")
    public String getOwnerFragment(@PathVariable String fragment) {
        return "fragments/owner/" + fragment;
    }

    @GetMapping("/owner-dashboard")
    public String ownerDashboard(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return "redirect:/login";
        }

        String profileImageUrl = "https://i.kym-cdn.com/entries/icons/facebook/000/049/273/cover11.jpg";
        if (member.getProfileImage() != null) {
            profileImageUrl = ImagePathParser.parse(member.getProfileImage().getUuidFileName());
        }

        model.addAttribute("profileImageUrl", profileImageUrl);
        model.addAttribute("memberName", member.getName());
        model.addAttribute("restaurantId", restaurantService.getRestaurantByOwnerId(member.getId()).getId());

        return "owner/dashboard";
    }
}
