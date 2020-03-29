package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Locale;
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
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        return new ResponseEntity<>(advertisementService.findAll(), HttpStatus.OK);
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
                                                         Principal principal,
                                                         HttpServletRequest request) {

        long subcategoryId = dto.getProduct().getSubcategoryId();

        validateNewAdvertisementInternalEntitiesIdsAreZero(dto);
        validateImageUrlHasHotDuplicate(dto);
        validateSubcategoryId(subcategoryId, request);

        return new ResponseEntity<>(advertisementService.createAdvertisement(dto,
                getUser(principal.getName())),
                HttpStatus.CREATED);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<AdvertisementDto> updateAdvertisement(@Valid @RequestBody AdvertisementDto dto,
                                                         Principal principal,
                                                         HttpServletRequest request) {

        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        long subcategoryId = dto.getProduct().getSubcategoryId();
        validateSubcategoryId(subcategoryId, request);

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
            throw new SecurityException(messageSource.getMessage("user.not-owner",
                    null,
                    Locale.getDefault()) + dto);
        }
    }

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("user.not-found",
                        null,
                        Locale.getDefault()) + userNameOrEmail));
    }

    private void validateImageUrlHasHotDuplicate(AdvertisementDto advertisementDto) {
        List<String> imagesUrls = advertisementDto.getProduct().getImages().stream()
                .map(ImageDto::getResourceUrl).collect(Collectors.toList());

        imagesUrls.forEach(imageUrl -> {
            if (advertisementService.isImageUrlHasDuplicate(imageUrl)) {
                throw new InvalidDtoException(messageSource.getMessage("image-url.has.duplicate",
                        null,
                        Locale.getDefault()) + imageUrl);
            }
        });
    }

    private void validateSubcategoryId(long subcategoryId,
                                       HttpServletRequest request) {

        final String contextUrl = "/subcategory/exist/";
        RestTemplate restTemplate = new RestTemplate();

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        String absoluteUrl = baseUrl + contextUrl + subcategoryId;

        boolean isSubcategoryExists = Optional.ofNullable(restTemplate.getForObject(absoluteUrl, Boolean.class))
                .orElse(false);

        if (subcategoryId == 0 || !isSubcategoryExists) {
            throw new IllegalIdentifierException(messageSource.getMessage("invalid.subcategory.id",
                    null,
                    Locale.getDefault()) + subcategoryId);
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

        imagesIds.forEach(imageId -> {
            validateNewEntityIdIsZero(imageId, "new.image.id.not-zero");
        });
    }

    private void validateNewEntityIdIsZero(long id, String errorMessage) {
        if (id != 0) {
            throw new IllegalIdentifierException(messageSource.getMessage(errorMessage,
                    null,
                    Locale.getDefault()) + id);
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
                .body(messageSource.getMessage("entity.not-found",
                        null,
                        Locale.getDefault()) + e.getLocalizedMessage());
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
                .body(messageSource.getMessage("sql.exception",
                        null,
                        Locale.getDefault()) + e.getLocalizedMessage());
    }
}
