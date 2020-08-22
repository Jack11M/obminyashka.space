package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@Validated
@Slf4j
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return new ResponseEntity<>(locationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{location_id}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable("location_id")
                                                   @PositiveOrZero(message = "{invalid.id}") long id) {
        try {
            LocationDto locationDto = locationService.getById(id);
            return new ResponseEntity<>(locationDto, HttpStatus.OK);
        } catch (ClassNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto) {
        long id = locationDto.getId();
        if (id != 0) {
            throw new IllegalIdentifierException(getExceptionMessageSourceWithId(id, "new.location.id.not-zero"));
        }
        return new ResponseEntity<>(locationService.save(locationDto), HttpStatus.CREATED);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<LocationDto> updateLocation(@Valid @RequestBody LocationDto locationDto) {
        if (!locationService.existsById(locationDto.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationService.update(locationDto), HttpStatus.OK);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteLocations(@RequestParam("ids") List<Long> locationIds) {
        boolean isAllMatch = locationIds.stream().allMatch(locationService::existsById);
        if (!isAllMatch) {
            throw new IllegalIdentifierException(getExceptionMessageSource("exception.illegal.id"));
        }
        locationService.removeById(locationIds);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}