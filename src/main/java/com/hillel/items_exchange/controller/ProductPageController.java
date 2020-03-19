package com.hillel.items_exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductPageController {

    @GetMapping("/create")
    public String getCreate() {
        return "create";
    }

    @GetMapping("/info")
    public String getInfo() {
        return "info";
    }
}
