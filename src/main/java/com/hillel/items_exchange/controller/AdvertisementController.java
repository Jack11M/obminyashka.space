package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.exception.DataConflictException;
import com.hillel.items_exchange.exception.IllegalIdentifierException;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.service.UserService;
import com.hillel.items_exchange.util.MessageSourceUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    @ApiOperation(value = "Find requested quantity of the advertisement and return them as a page result")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<AdvertisementDto>> findPaginated(
            @ApiParam(value = "Results page you want to retrieve (0..N). Default value: 0")
                @RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero int page,
            @ApiParam(value = "Number of records per page. Default value: 12")
                @RequestParam(value = "size", required = false, defaultValue = "12") @PositiveOrZero int size){
        List<AdvertisementDto> dtoList = advertisementService.findAll(PageRequest.of(page, size));
        return dtoList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{advertisement_id}")
    @ApiOperation(value = "Find an advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<AdvertisementDto> getAdvertisement(
            @ApiParam(value = "ID of existed advertisement")
            @PathVariable("advertisement_id") @PositiveOrZero(message = "{invalid.id}") Long id) {
        return ResponseEntity.of(advertisementService.findDtoById(id));
    }

    @GetMapping("/topic/{topic}")
    @ApiOperation(value = "Find first 10 advertisements by topic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<AdvertisementDto>> getFirst10AdvertisementsByTopic(@PathVariable("topic") @NotEmpty String topic) {
        List<AdvertisementDto> allByTopic = advertisementService.findFirst10ByTopic(topic);
        return allByTopic.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(allByTopic, HttpStatus.OK);
    }

    @PostMapping("/filter")
    @ApiOperation(value = "Filter advertisements by multiple params and return up to 10 results")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<AdvertisementDto>> getFirst10BySearchParameters(@Valid @RequestBody AdvertisementFilterDto advertisementFilterDto) {
        List<AdvertisementDto> advertisementsByMultipleParams = advertisementService.findFirst10AdvertisementsByMultipleParams(advertisementFilterDto);
        return advertisementsByMultipleParams.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(advertisementsByMultipleParams, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto createAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal)
            throws IllegalIdentifierException {

        long subcategoryId = dto.getSubcategoryId();

        validateNewAdvertisementInternalEntitiesIdsAreZero(dto);
        validateSubcategoryId(subcategoryId);

        return advertisementService.createAdvertisement(dto, getUser(principal.getName()));
    }

    @PutMapping
    @ApiOperation(value = "Update an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 409, message = "CONFLICT")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AdvertisementDto updateAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal)
            throws DataConflictException, IllegalIdentifierException {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        long subcategoryId = dto.getSubcategoryId();
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
    public ResponseEntity<HttpStatus> deleteAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal)
            throws DataConflictException {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        Optional<AdvertisementDto> byId = advertisementService.findDtoById(dto.getId());

        if (byId.isPresent() && byId.get().equals(dto)) {
            advertisementService.remove(dto.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/default-image/{advertisementId}/{imageId}")
    @ApiOperation(value = "Set a default image to an existed advertisement")
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
        if (!advertisementService.isUserHasAdvertisementAndItHasImageById(advertisementId, imageId, owner)) {
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

    private void validateAdvertisementOwner(AdvertisementDto dto, User owner) throws DataConflictException {

        if (!advertisementService.isUserHasAdvertisementWithId(dto.getId(), owner)) {
            throw new DataConflictException(getExceptionMessageSource("user.not-owner"));
        }
    }

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        getExceptionMessageSourceWithAdditionalInfo("user.not-found", userNameOrEmail)));
    }

    private void validateSubcategoryId(long subcategoryId) throws IllegalIdentifierException {
        boolean isSubcategoryExists = subcategoryService.isSubcategoryExistsById(subcategoryId);

        if (subcategoryId == 0 || !isSubcategoryExists) {
            throw new IllegalIdentifierException(getExceptionMessageSourceWithId(subcategoryId,
                    "invalid.subcategory.id"));
        }
    }

    private void validateNewAdvertisementInternalEntitiesIdsAreZero(AdvertisementDto dto) throws IllegalIdentifierException {
        long advertisementId = dto.getId();
        long locationId = dto.getLocation().getId();
        List<Long> imagesIds = dto.getImages().stream()
                .map(ImageDto::getId)
                .collect(Collectors.toList());

        validateNewEntityIdIsZero(advertisementId, "new.advertisement.id.not-zero");
        validateNewEntityIdIsZero(locationId, "new.location.id.not-zero");

        boolean isAllIdsEqualZero = imagesIds.stream().allMatch(imageId -> imageId == 0);
        if(!isAllIdsEqualZero){
            throw new IllegalIdentifierException(MessageSourceUtil.getExceptionMessageSource("new.image.id.not-zero"));
        }
    }

    private void validateNewEntityIdIsZero(long id, String errorMessage) throws IllegalIdentifierException {
        if (id != 0) {
            throw new IllegalIdentifierException(getExceptionMessageSourceWithId(id, errorMessage));
        }
    }
}
