package com.hillel.items_exchange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
