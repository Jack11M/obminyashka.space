package space.obminyashka.items_exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.AdvertisementDisplayDto;
import space.obminyashka.items_exchange.dto.AdvertisementFilterDto;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.dto.AdvertisementTitleDto;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.CategorySizeNotFoundException;
import space.obminyashka.items_exchange.exception.IllegalIdentifierException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.*;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

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

    @GetMapping(ApiKey.ADV_THUMBNAIL)
    @Operation(summary = "Find requested quantity of the advertisement as thumbnails and return them as a page result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public Page<AdvertisementTitleDto> findPaginatedAsThumbnails(
            @Parameter(name = "Results page you want to retrieve (0..N). Default value: 0")
            @RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero int page,
            @Parameter(name = "Number of records per page. Default value: 12")
            @RequestParam(value = "size", required = false, defaultValue = "12") @PositiveOrZero int size) {
        return advertisementService.findAllThumbnails(PageRequest.of(page, size));
    }

    @GetMapping(ApiKey.ADV_THUMBNAIL_RANDOM)
    @Operation(summary = "Find 12 random advertisement as thumbnails and return them as a result")
    @ApiResponse(responseCode = "200", description = "OK")
    public List<AdvertisementTitleDto> findRandom12Thumbnails() {
        return advertisementService.findRandom12Thumbnails();
    }

    @GetMapping(ApiKey.ADV_TOTAL)
    @Operation(summary = "Count existed advertisements and return total records amount number")
    @ApiResponse(responseCode = "200", description = "OK")
    public Long countAdvertisements() {
        return advertisementService.count();
    }

    @GetMapping(ApiKey.ADV_ID)
    @Operation(summary = "Find an advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<AdvertisementDisplayDto> getAdvertisement(
            @Parameter(name = "ID of existed advertisement")
            @PathVariable("advertisement_id") UUID id) {
        return ResponseEntity.of(advertisementService.findDtoById(id));
    }

    @GetMapping(ApiKey.ADV_SEARCH_PAGINATED)
    @Operation(summary = "Find advertisements by keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<Page<AdvertisementTitleDto>> getPageOfAdvertisementsByKeyword(
            @PathVariable @NotEmpty String keyword,
            @Parameter(name = "Results page you want to retrieve (0..N). Default value: 0")
            @RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero int page,
            @Parameter(name = "Number of records per page. Default value: 12")
            @RequestParam(value = "size", required = false, defaultValue = "12") @PositiveOrZero int size) {
        Page<AdvertisementTitleDto> allByKeyword = advertisementService.findByKeyword(keyword, PageRequest.of(page, size, Sort.by("topic")));
        return allByKeyword.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(allByKeyword, HttpStatus.OK);
    }

    @GetMapping(ApiKey.ADV_SEARCH_PAGINATED_BY_CATEGORY_ID)
    @Operation(summary = "Find advertisements by category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public Page<AdvertisementTitleDto> getPageOfAdvertisementsByCategoryId(
            @PathVariable("category_id") Long id,
            @Parameter(name = "Results page you want to retrieve (0..N). Default value: 0")
            @RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero int page,
            @Parameter(name = "Number of records per page. Default value: 12")
            @RequestParam(value = "size", required = false, defaultValue = "12") @PositiveOrZero int size) throws CategorySizeNotFoundException {
        return Optional.of(advertisementService.findByCategoryId(id, PageRequest.of(page, size)))
                .filter(Predicate.not(Page::isEmpty))
                .orElseThrow(() -> new CategorySizeNotFoundException(
                        getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_CATEGORY_ID)));

    }

    @PostMapping(ApiKey.ADV_FILTER)
    @Operation(summary = "Filter advertisements by multiple params and return up to 10 results.\n" +
            "Fill only needed parameters.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementTitleDto> getFirst10BySearchParameters(@Valid @RequestBody AdvertisementFilterDto filterDto) {
        return advertisementService.findFirst10ByFilter(filterDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(value = ApiKey.ADV,
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Operation(summary = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementModificationDto createAdvertisement(
            @Valid @RequestPart AdvertisementModificationDto dto,
            @RequestPart(value = "image") @Size(min = 1, max = 10) List<MultipartFile> images,
            @Parameter(hidden = true) Authentication authentication) throws IllegalIdentifierException {

        validateInternalEntityIds(dto.getSubcategoryId(), dto.getLocationId());
        final var owner = getUser(authentication.getName());
        final var compressedImages = images.parallelStream()
                .map(imageService::compress)
                .toList();
        return advertisementService.createAdvertisement(dto, owner, compressedImages);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(ApiKey.ADV_ID)
    @Operation(summary = "Update an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description= "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode= "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AdvertisementModificationDto updateAdvertisement(@PathVariable("advertisement_id") UUID id,
                                                            @Valid @RequestBody AdvertisementModificationDto dto,
                                                            @Parameter(hidden = true) Authentication authentication)
            throws IllegalIdentifierException, IllegalOperationException {

        validateAdvertisementOwner(id, getUser(authentication.getName()));
        validateInternalEntityIds(dto.getSubcategoryId(), dto.getLocationId());
        dto.setId(id);
        return advertisementService.updateAdvertisement(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(ApiKey.ADV_ID)
    @Operation(summary = "Delete an existed advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdvertisement(@PathVariable("advertisement_id") UUID id,
                                    @Parameter(hidden = true) Authentication authentication) throws IllegalOperationException {
        User owner = getUser(authentication.getName());
        validateAdvertisementOwner(id, owner);
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
            @Parameter(name = "ID of existed advertisement") @PathVariable UUID advertisementId,
            @Parameter(name = "ID of existed image") @PathVariable UUID imageId,
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

    private void validateAdvertisementOwner(UUID advertisementId, User owner) throws IllegalOperationException {
        if (!advertisementService.isUserHasAdvertisementWithId(advertisementId, owner)) {
            throw new IllegalOperationException(getMessageSource(
                    ResponseMessagesHandler.ValidationMessage.USER_NOT_OWNER));
        }
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
