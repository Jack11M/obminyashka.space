package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/adv")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        return new ResponseEntity<>(advertisementService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{advertisement_id}")
    public @ResponseBody ResponseEntity<AdvertisementDto> getAdvertisement(@PathVariable("advertisement_id") Long id) {
        return advertisementService.findById(id)
                .map(advertisementDto -> new ResponseEntity<>(advertisementDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public @ResponseBody ResponseEntity<AdvertisementDto> createAdvertisement(@Valid @RequestBody AdvertisementDto dto) {
        if (dto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(advertisementService.createAdvertisement(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public @ResponseBody ResponseEntity<AdvertisementDto> updateAdvertisement(@Valid @RequestBody AdvertisementDto dto) {
        if (isAdvertisementIdInvalid(dto.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(advertisementService.updateAdvertisement(dto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAdvertisement(@Valid @RequestBody AdvertisementDto dto) {
        if (isAdvertisementIdInvalid(dto.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        advertisementService.remove(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private boolean isAdvertisementIdInvalid(Long id) {
        return id == null || id <= 0L;
    }
}
