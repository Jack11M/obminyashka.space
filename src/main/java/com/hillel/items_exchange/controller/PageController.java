package com.hillel.items_exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @GetMapping("/search")
    public String getResult() {
        return "result";
    }

}
