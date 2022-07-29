package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.dto.RawLocation;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.mapper.UtilMapper;
import space.obminyashka.items_exchange.service.LocationService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static space.obminyashka.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithAdditionalInfo;

@RestController
@Api(tags = "Location")
@RequiredArgsConstructor
@Validated
@Slf4j
public class LocationController {
    private final LocationService locationService;

    @GetMapping(ApiKey.LOCATION)
    @ApiOperation(value = "Get all of existed locations.")
    @ResponseStatus(HttpStatus.OK)
    public List<LocationDto> getAllLocations() {
        return locationService.findAll();
    }

    @GetMapping(ApiKey.LOCATION_ALL)
    @ApiOperation(value = "Get all locations for current locale (I18n).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST")})
    @ApiImplicitParam(name = HttpHeaders.ACCEPT_LANGUAGE, value = "Localization header", paramType = "header",
            required = true, dataTypeClass = String.class, allowableValues = "ua, ru, en", defaultValue = "ua")
    public List<LocationDto> getAllLocationsForCurrentLanguage() throws BadRequestException {
        final var locale = LocaleContextHolder.getLocale();
        final var language = locale.getLanguage();
        if (!Set.of("ua", "ru", "en").contains(language)) {
            throw new BadRequestException("Received locale is not supported");
        }
        return locationService.findAllForCurrentLanguage(locale);
    }

    @GetMapping(ApiKey.LOCATION_ID)
    @ApiOperation(value = "Get an existed location by its ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<LocationDto> getLocation(@PathVariable("location_id") UUID id) {
        return ResponseEntity.of(locationService.getById(id));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping(ApiKey.LOCATION)
    @ApiOperation(value = "Save a new Location", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto) {
        return new ResponseEntity<>(locationService.save(locationDto), HttpStatus.CREATED);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping(ApiKey.LOCATION_ID)
    @ApiOperation(value = "Update an existed Location", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<LocationDto> updateLocation(@PathVariable("location_id") UUID locationId,
                                                      @Valid @RequestBody LocationDto locationDto) {
        locationDto.setId(locationId);
        return locationService.existsById(locationId) ?
                new ResponseEntity<>(locationService.update(locationDto), HttpStatus.ACCEPTED) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping(ApiKey.LOCATION)
    @ApiOperation(value = "Delete existed Locations by their IDs", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteLocations(@RequestParam("ids") List<UUID> locationIds) {
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
    @PostMapping(ApiKey.LOCATIONS_INIT)
    @ApiOperation(value = "Setting up locations from request", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createLocationsInitFile(@RequestBody List<RawLocation> rawLocationList) {
        try {
            return new ResponseEntity<>(locationService.createParsedLocationsFile(rawLocationList), HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
