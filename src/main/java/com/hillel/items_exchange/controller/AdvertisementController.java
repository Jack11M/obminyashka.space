package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.service.UserService;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.util.MessageSourceUtil.*;

@RestController
@RequestMapping("/adv")
@Api(tags = "Advertisement")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final SubcategoryService subcategoryService;

    @GetMapping(params = {"page", "size"})
    @ApiOperation(value = "Find requested quantity of the advertisement and return them as a page result")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST")})
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementDto> findPaginated(
            @ApiParam(value = "Number of a result's page (starts from 0)")
            @RequestParam("page") @PositiveOrZero int page,
            @ApiParam(value = "Size of results for returning (starts from 1)")
            @RequestParam("size") @PositiveOrZero int size) {
        return advertisementService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{advertisement_id}")
    @ApiOperation(value = "Find an advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<AdvertisementDto> getAdvertisement(
            @ApiParam(value = "ID of existed advertisement") @PathVariable("advertisement_id") Long id) {
        return advertisementService.findById(id)
                .map(advertisementDto -> new ResponseEntity<>(advertisementDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/topic/{topic}")
    @ApiOperation(value = "Find advertisements by topic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisementsByTopic(@PathVariable("topic") @NotEmpty String topic) {
        List<AdvertisementDto> allByTopic = advertisementService.findAllByTopic(topic);
        return allByTopic.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(allByTopic, HttpStatus.OK);
    }

    @PostMapping("/filter")
    @ApiOperation(value = "Filter advertisements by multiple params")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST")})
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementDto> getAllBySearchParameters(@Valid @RequestBody AdvertisementFilterDto advertisementFilterDto) {
        return advertisementService.findAdvertisementsByMultipleParams(advertisementFilterDto);
    }

    @PostMapping
    @ApiOperation(value = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto createAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {

        long subcategoryId = dto.getProduct().getSubcategoryId();

        validateNewAdvertisementInternalEntitiesIdsAreZero(dto);
        validateSubcategoryId(subcategoryId);

        return advertisementService.createAdvertisement(dto, getUser(principal.getName()));
    }

    @PutMapping
    @ApiOperation(value = "Update an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AdvertisementDto updateAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        long subcategoryId = dto.getProduct().getSubcategoryId();
        validateSubcategoryId(subcategoryId);

        return advertisementService.updateAdvertisement(dto);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 409, message = "CONFLICT")})
    public ResponseEntity<HttpStatus> deleteAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        Optional<AdvertisementDto> byId = advertisementService.findById(dto.getId());

        if (byId.isPresent() && byId.get().equals(dto)) {
            advertisementService.remove(dto.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.unprocessableEntity().body(HttpStatus.CONFLICT);
    }

    @PostMapping("/default-image/{advertisementId}/{imageId}")
    @ApiOperation(value = "Set default image to the an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE")})
    public ResponseEntity<HttpStatus> setDefaultImage(
            @ApiParam(value = "ID of existed advertisement")
            @PathVariable @PositiveOrZero(message = "{invalid.id}") Long advertisementId,
            @ApiParam(value = "ID of existed image")
            @PathVariable @PositiveOrZero(message = "{invalid.id}") Long imageId,
            Principal principal) {
        User owner = getUser(principal.getName());
        if (!advertisementService.isAdvertisementAndImageExists(advertisementId, imageId, owner)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            advertisementService.setDefaultImage(advertisementId, imageId, owner);
        } catch (ClassNotFoundException e) {
            log.warn(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateAdvertisementOwner(AdvertisementDto dto, User owner) {

        if (!advertisementService.isAdvertisementExists(dto.getId(), owner)) {
            throw new SecurityException(getExceptionMessageSource("user.not-owner"));
        }
    }

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        getExceptionMessageSourceWithAdditionalInfo("user.not-found", userNameOrEmail)));
    }

    private void validateSubcategoryId(long subcategoryId) {
        boolean isSubcategoryExists = subcategoryService.isSubcategoryExistsById(subcategoryId);

        if (subcategoryId == 0 || !isSubcategoryExists) {
            throw new IllegalIdentifierException(getExceptionMessageSourceWithId(subcategoryId,
                    "invalid.subcategory.id"));
        }
    }

    private void validateNewAdvertisementInternalEntitiesIdsAreZero(@RequestBody @Valid AdvertisementDto dto) {
        long advertisementId = dto.getId();
        long locationId = dto.getLocation().getId();
        long productId = dto.getProduct().getId();
        List<Long> imagesIds = dto.getProduct().getImages().stream()
                .map(ImageDto::getId)
                .collect(Collectors.toList());

        validateNewEntityIdIsZero(advertisementId, "new.advertisement.id.not-zero");
        validateNewEntityIdIsZero(locationId, "new.location.id.not-zero");
        validateNewEntityIdIsZero(productId, "new.product.id.not-zero");

        imagesIds.forEach(imageId -> validateNewEntityIdIsZero(imageId, "new.image.id.not-zero"));
    }

    private void validateNewEntityIdIsZero(long id, String errorMessage) {
        if (id != 0) {
            throw new IllegalIdentifierException(getExceptionMessageSourceWithId(id, errorMessage));
        }
    }
}
