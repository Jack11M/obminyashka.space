package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private static final String NAME_OF_CLASS = "IN the ImageController: ";
    private final ImageService imageService;

    @GetMapping("/{product_id}/imageUrls")
    public ResponseEntity<List<String>> getImageUrlsByProductId(@PathVariable("product_id") long id) {
        List<String> imageUrls = imageService.getImageUrlsByProductId(id);
        return (imageUrls.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    @GetMapping("/{product_id}/images")
    public ResponseEntity<List<ImageDto>> getImagesByProductId(@PathVariable("product_id") long id) {
        List<ImageDto> images = imageService.getByProductId(id);
        return (images.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(images, HttpStatus.OK);
    }
}