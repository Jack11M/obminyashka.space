package com.hillel.evoApp.controller;

import com.hillel.evoApp.dto.AdvertisementDto;
import com.hillel.evoApp.model.Advertisement;
import com.hillel.evoApp.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    public List<Advertisement> getAllAdvertisements() {
        return advertisementService.findAll();
    }

    @GetMapping("/{advertisement_id}")
    public ResponseEntity<Advertisement> getAdvertisement(@PathVariable("advertisement_id") Long id) {
        return ResponseEntity.of(advertisementService.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Advertisement> createAdvertisement(@Validated @RequestParam("advertisement") AdvertisementDto dto) {
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(dto);
        return new ResponseEntity<>(createdAdvertisement, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Advertisement> updateAdvertisement(@Validated @RequestParam("advertisement") AdvertisementDto dto) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisement(dto);
        return new ResponseEntity<>(updatedAdvertisement, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteAdvertisement(@RequestParam("advertisement") AdvertisementDto dto) {
        advertisementService.remove(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
