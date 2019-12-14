package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/adv")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

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
    public @ResponseBody
    ResponseEntity<AdvertisementDto> createAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {
        if (dto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(advertisementService.createAdvertisement(dto, getUser(principal.getName())), HttpStatus.CREATED);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<AdvertisementDto> updateAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {
        if (isAdvertisementIdInvalid(dto.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User owner = getUser(principal.getName());
        if (!advertisementService.isAdvertisementExists(dto.getId(), owner)) {
            throw new EntityNotFoundException("Current user don't have advertisement with such id");
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

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + userNameOrEmail + " not found!"));
    }
}
