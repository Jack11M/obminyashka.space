package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.LocationDto;
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
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithAdditionalInfo;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

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
    @ResponseStatus(HttpStatus.OK)
    public List<LocationDto> getAllLocations() {
        return locationService.findAll();
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
    @ApiOperation(value = "Save a new Location. (ADMIN ONLY)")
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
    @ApiOperation(value = "Update an existed Location. (ADMIN ONLY)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<LocationDto> updateLocation(@Valid @RequestBody LocationDto locationDto) {
        if (!locationService.existsById(locationDto.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationService.update(locationDto), HttpStatus.OK);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping
    @ApiOperation(value = "Delete existed Locations by their IDs. (ADMIN ONLY)")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<HttpStatus> deleteLocations(@RequestParam("ids") List<Long> locationIds) {
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
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}