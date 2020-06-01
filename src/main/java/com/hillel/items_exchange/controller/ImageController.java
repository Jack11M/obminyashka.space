package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{product_id}/links")
    public ResponseEntity<List<String>> getImageLinksByProductId(@PathVariable("product_id")
                                                                @PositiveOrZero(message = "{invalid.id}") long id) {
        List<String> links = imageService.getLinksByProductId(id);
        return (links.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(links, HttpStatus.OK);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<List<ImageDto>> getByProductId(@PathVariable("product_id")
                                             @PositiveOrZero(message = "{invalid.id}") long id) {
        List<ImageDto> images = imageService.getByProductId(id);
        return (images.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(images, HttpStatus.OK);
    }
}