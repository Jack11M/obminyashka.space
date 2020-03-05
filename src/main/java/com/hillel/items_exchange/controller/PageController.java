package com.hillel.items_exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {
    @GetMapping("/temp")
    public String getTemp() {
        return "temp";
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @GetMapping("/search")
    public String getResult() {
        return "result";
    }

    @GetMapping("/product/create")
    public String getCreate() {
        return "create";
    }

    @GetMapping("/product/info")
    public String getInfo() {
        return "info";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    @GetMapping("/profile/favorites")
    public String getFavorites() {
        System.out.println("start method getFavorites");
        return "favorites";
    }

    @GetMapping("/profile/settings")
    public String getSettings() {
        return "settings";
    }
}
