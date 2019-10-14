package com.hillel.evoApp.controller;

import com.hillel.evoApp.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/adv")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @ResponseBody
    @GetMapping("/categories")
    public List<String> allCategories() {
        return advertisementService.findAllCategoryNames();
    }
}
