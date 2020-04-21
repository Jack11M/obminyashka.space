package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.service.UserService;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/adv")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Log4j2
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final SubcategoryService subcategoryService;
    private final MessageSourceUtil messageSourceUtil;

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<AdvertisementDto>> findPaginated(@RequestParam("page") @PositiveOrZero int page,
                                                                @RequestParam("size") @PositiveOrZero int size) {
        return new ResponseEntity<>(advertisementService.findAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{advertisement_id}")
    public @ResponseBody
    ResponseEntity<AdvertisementDto> getAdvertisement(@PathVariable("advertisement_id") Long id) {
        return advertisementService.findById(id)
                .map(advertisementDto -> new ResponseEntity<>(advertisementDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/topic/{topic}")
    public @ResponseBody
    ResponseEntity<List<AdvertisementDto>> getAllAdvertisementsByTopic(@PathVariable("topic") @NotEmpty String topic) {
        List<AdvertisementDto> allByTopic = advertisementService.findAllByTopic(topic);
        if (allByTopic.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allByTopic, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public @ResponseBody
    ResponseEntity<List<AdvertisementDto>> getAllBySearchParameters(@Valid @RequestBody AdvertisementFilterDto
                                                                            advertisementFilterDto) {
        return new ResponseEntity<>(advertisementService.findAdvertisementsByMultipleParams(advertisementFilterDto),
                HttpStatus.OK);
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<AdvertisementDto> createAdvertisement(@Valid @RequestBody AdvertisementDto dto,
                                                         Principal principal) {

        long subcategoryId = dto.getProduct().getSubcategoryId();

        validateNewAdvertisementInternalEntitiesIdsAreZero(dto);
        validateImageUrlHasHotDuplicate(dto);
        validateSubcategoryId(subcategoryId);

        return new ResponseEntity<>(advertisementService.createAdvertisement(dto,
                getUser(principal.getName())),
                HttpStatus.CREATED);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<AdvertisementDto> updateAdvertisement(@Valid @RequestBody AdvertisementDto dto,
                                                         Principal principal) {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        long subcategoryId = dto.getProduct().getSubcategoryId();
        validateSubcategoryId(subcategoryId);

        return new ResponseEntity<>(advertisementService.updateAdvertisement(dto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAdvertisement(@Valid @RequestBody AdvertisementDto dto,
                                                          Principal principal) {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        Optional<AdvertisementDto> byId = advertisementService.findById(dto.getId());

        if (byId.isPresent() && byId.get().equals(dto)) {
            advertisementService.remove(dto.getId());
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.unprocessableEntity().body(HttpStatus.CONFLICT);
    }

    private void validateAdvertisementOwner(@RequestBody @Valid AdvertisementDto dto,
                                            User owner) {

        if (!advertisementService.isAdvertisementExists(dto.getId(), owner)) {
            throw new SecurityException(messageSourceUtil.getExceptionMessageSource("user.not-owner"));
        }
    }

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(messageSourceUtil
                        .getExceptionMessageSourceWithAdditionalInfo("user.not-found", userNameOrEmail)));
    }

    private void validateImageUrlHasHotDuplicate(AdvertisementDto advertisementDto) {
        List<String> imagesUrls = advertisementDto.getProduct().getImages().stream()
                .map(ImageDto::getResourceUrl).collect(Collectors.toList());

        imagesUrls.forEach(imageUrl -> {
            if (advertisementService.isImageUrlHasDuplicate(imageUrl)) {
                throw new InvalidDtoException(messageSourceUtil
                        .getExceptionMessageSourceWithAdditionalInfo("image-url.has.duplicate", imageUrl));
            }
        });
    }

    private void validateSubcategoryId(long subcategoryId) {
        boolean isSubcategoryExists = subcategoryService.isSubcategoryExistsById(subcategoryId);

        if (subcategoryId == 0 || !isSubcategoryExists) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSourceWithId(subcategoryId,
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
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSourceWithId(id, errorMessage));
        }
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsernameNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleOwnerException(SecurityException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleOwnerException(EntityNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageSourceUtil.getExceptionMessageSourceWithAdditionalInfo("entity.not-found",
                        e.getLocalizedMessage()));
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<String> handleIdException(IllegalIdentifierException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidDtoException.class)
    public ResponseEntity<String> handleInvalidAdvertisementDtoException(InvalidDtoException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSqlException(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageSourceUtil.getExceptionMessageSourceWithAdditionalInfo("sql.exception",
                        e.getLocalizedMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        log.warn(errorMessage, e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
}
