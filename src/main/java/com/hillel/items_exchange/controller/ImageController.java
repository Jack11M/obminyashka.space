package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/adv/{advertisement_id}/product/{product_id}/image-urls")
    public ResponseEntity<List<String>> getImageUrlsByProductId(@PathVariable("product_id") long id) {
        List<String> imageUrls = imageService.getImageUrlsByProductId(id);
        return (imageUrls.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    @GetMapping("/adv/{advertisement_id}/product/{product_id}/images")
    public ResponseEntity<List<ImageDto>> getByAdvertisementIdAndProductId(@PathVariable("advertisement_id") long advId,
                                                                           @PathVariable("product_id") long productId) {
        List<ImageDto> images = imageService.getByAdvertisementIdAndProductId(advId, productId);
        return (images.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(images, HttpStatus.OK);
    }
}