package com.hillel.evoApp.controller;

import com.hillel.evoApp.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adv")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

}
