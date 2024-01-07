package space.obminyashka.items_exchange.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;
import space.obminyashka.items_exchange.rest.exception.bad_request.BadRequestException;
import space.obminyashka.items_exchange.rest.exception.bad_request.IllegalIdentifierException;
import space.obminyashka.items_exchange.rest.request.AdvertisementFilterRequest;
import space.obminyashka.items_exchange.rest.response.AdvertisementDisplayView;
import space.obminyashka.items_exchange.rest.response.AdvertisementTitleView;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;
import space.obminyashka.items_exchange.service.*;

import java.util.List;
import java.util.UUID;

import static space.obminyashka.items_exchange.repository.enums.Size.*;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.*;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@RestController
@Tag(name = "Advertisement")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final ImageService imageService;
    private final UserService userService;
    private final SubcategoryService subcategoryService;
    private final LocationService locationService;

    @GetMapping(value = ApiKey.ADV_TOTAL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Count existed advertisements and return total records amount number")
    @ApiResponse(responseCode = "200", description = "OK")
    public Long countAdvertisements() {
        return advertisementService.count();
    }

    @GetMapping(value = ApiKey.ADV_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find an advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<AdvertisementDisplayView> getAdvertisement(
            @Parameter(name = "advertisement_id", description = "ID of existed advertisement")
            @PathVariable("advertisement_id") UUID id) {
        return ResponseEntity.of(advertisementService.findDtoById(id));
    }

    @PostMapping(value = ApiKey.ADV_FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find advertisements by multiple params")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<Page<AdvertisementTitleView>> filterAdvertisementBySearchParameters(@Valid @RequestBody AdvertisementFilterRequest advertisementFilterRequest) {
        Page<AdvertisementTitleView> advertisements = advertisementService.filterAdvertisementBySearchParameters(advertisementFilterRequest);
        log.info("[filter] Response count: {}", advertisements.stream().count());
        return advertisements.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(value = ApiKey.ADV,
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementModificationDto createAdvertisement(
            @Valid @RequestPart AdvertisementModificationDto dto,
            @RequestPart(value = "image") @jakarta.validation.constraints.Size(min = 1, max = 10) List<MultipartFile> images,
            @Parameter(hidden = true) Authentication authentication) throws IllegalIdentifierException {

        isValidSize(dto.getSubcategoryId(), dto.getSize());
        validateInternalEntityIds(dto.getSubcategoryId(), dto.getLocationId());
        final var owner = getUser(authentication.getName());
        final var compressedImages = images.parallelStream()
                .map(imageService::compress)
                .toList();
        byte[] scaledTitleImage = images.stream().findFirst()
                .map(imageService::scale)
                .orElse(compressedImages.getFirst());
        return advertisementService.createAdvertisement(dto, owner, compressedImages, scaledTitleImage);
    }

    private void isValidSize(long subcategoryId, String size) {
        Range<Long> clothingSubcategory = Range.closed(1L, 12L); // subcategories ID for Clothing
        Range<Long> shoesSubcategory = Range.closed(13L, 17L); // subcategories ID for Shoes

        if (clothingSubcategory.contains(subcategoryId)) {
            Clothing.fromValue(size);
        } else if (shoesSubcategory.contains(subcategoryId)) {
            try {
                Shoes.fromValue(Double.parseDouble(size));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(getParametrizedMessageSource(INVALID_ENUM_VALUE, size));
            }
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.ADV_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AdvertisementModificationDto updateAdvertisement(@PathVariable("advertisement_id") UUID id,
                                                            @Valid @RequestBody AdvertisementModificationDto dto,
                                                            @Parameter(hidden = true) Authentication authentication)
            throws IllegalIdentifierException, IllegalOperationException {

        isValidSize(dto.getSubcategoryId(), dto.getSize());
        advertisementService.validateUserAsAdvertisementOwner(id, authentication.getName());
        validateInternalEntityIds(dto.getSubcategoryId(), dto.getLocationId());
        dto.setId(id);
        return advertisementService.updateAdvertisement(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(ApiKey.ADV_ID)
    @Operation(summary = "Delete an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdvertisement(@PathVariable("advertisement_id") UUID id,
                                    @Parameter(hidden = true) Authentication authentication)
            throws IllegalOperationException, EntityNotFoundException {

        advertisementService.validateUserAsAdvertisementOwner(id, authentication.getName());
        advertisementService.remove(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(ApiKey.ADV_DEFAULT_IMAGE)
    @Operation(summary = "Set a default image to an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void setDefaultImage(
            @Parameter(name = "advertisementId", description = "ID of existed advertisement") @PathVariable UUID advertisementId,
            @Parameter(name = "imageId", description = "ID of existed image") @PathVariable UUID imageId,
            @Parameter(hidden = true) Authentication authentication) throws BadRequestException {
        User owner = getUser(authentication.getName());
        if (!advertisementService.isUserHasAdvertisementAndItHasImageWithId(advertisementId, imageId, owner)) {
            throw new BadRequestException(getMessageSource(
                    ResponseMessagesHandler.ExceptionMessage.ADVERTISEMENT_IMAGE_ID_NOT_FOUND));
        }
        owner.getAdvertisements().parallelStream()
                .filter(advertisement -> advertisement.getId().equals(advertisementId))
                .findFirst()
                .ifPresent(adv -> advertisementService.setDefaultImage(adv, imageId));
    }

    private void validateInternalEntityIds(long subcategoryId, UUID locationId) throws IllegalIdentifierException {
        var exceptionMessage = "";
        if (!subcategoryService.isSubcategoryExistsById(subcategoryId)) {
            exceptionMessage = getExceptionMessageSourceWithId(subcategoryId, ResponseMessagesHandler.ValidationMessage.INVALID_SUBCATEGORY_ID) + "\n";
        }
        if (!locationService.existsById(locationId)) {
            exceptionMessage = exceptionMessage.concat(getExceptionMessageSourceWithId(subcategoryId, ResponseMessagesHandler.ValidationMessage.INVALID_LOCATION_ID));
        }
        if (!exceptionMessage.isEmpty()) {
            throw new IllegalIdentifierException(exceptionMessage);
        }
    }

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(getMessageSource(
                        ResponseMessagesHandler.ExceptionMessage.USER_NOT_FOUND)));
    }
}
