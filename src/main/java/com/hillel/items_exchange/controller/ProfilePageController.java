package com.hillel.items_exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfilePageController {
    @GetMapping()
    public String getProfile() {
        return "profile";
    }

    @GetMapping("/favorites")
    public String getFavorites() {
        return "favorites";
    }

    @GetMapping("/settings")
    public String getSettings() {
        return "settings";
    }
}
