package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.exception.InvalidLocationInitFileCreatingDataException;
import com.hillel.items_exchange.mapper.UtilMapper;
import com.hillel.items_exchange.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.*;

@RestController
@RequestMapping("/location")
@Api(tags = "Location")
@RequiredArgsConstructor
@Validated
@Slf4j
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @ApiOperation(value = "Get all of existed locations.")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> allLocations = locationService.findAll();
        return allLocations.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(allLocations, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all locations for current locale (I18n).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<LocationDto>> getAllLocationsForCurrentLanguage(
            @RequestHeader("accept-language") String lang) {

        List<LocationDto> locations = locationService.findAllForCurrentLanguage(lang);
        return locations.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/{location_id}")
    @ApiOperation(value = "Get an existed location by its ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<LocationDto> getLocation(@PathVariable("location_id")
                                                   @PositiveOrZero(message = "{invalid.id}") long id) {
        return ResponseEntity.of(locationService.getById(id));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    @ApiOperation(value = "Save a new Location", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto) {
        long id = locationDto.getId();
        if (id != 0) {
            log.warn("Location not created. Received ID={}", id);
            throw new IllegalIdentifierException(getExceptionMessageSourceWithId(id, "new.location.id.not-zero"));
        }
        return new ResponseEntity<>(locationService.save(locationDto), HttpStatus.CREATED);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping
    @ApiOperation(value = "Update an existed Location", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<LocationDto> updateLocation(@Valid @RequestBody LocationDto locationDto) {
        return locationService.existsById(locationDto.getId()) ?
                new ResponseEntity<>(locationService.update(locationDto), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping
    @ApiOperation(value = "Delete existed Locations by their IDs", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteLocations(@RequestParam("ids") List<Long> locationIds) {
        List<LocationDto> locations = locationService.findByIds(locationIds);
        if (locationIds.size() != locations.size()) {
            locationIds.removeAll(UtilMapper.mapBy(locations, LocationDto::getId));
            String strIds = locationIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ", ": ", ""));
            log.warn("Received nonexistent IDs {}", strIds);
            throw new IllegalIdentifierException(
                    getExceptionMessageSourceWithAdditionalInfo("exception.illegal.id", strIds));
        }
        locationService.removeById(locationIds);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping("/locations-init")
    @ApiOperation(value = "Setting up locations from request", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createLocationsInitFile(@RequestBody @NotEmpty String rawData) throws InvalidLocationInitFileCreatingDataException {
        if (!locationService.isLocationDataValid(rawData)) {
            throw new InvalidLocationInitFileCreatingDataException(
                    getMessageSource("exception.invalid.locations.file.creating.data"));
        }
        try {
            return new ResponseEntity<>(locationService.createParsedLocationsFile(rawData), HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
