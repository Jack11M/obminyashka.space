package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequiredArgsConstructor
@Validated
@Log4j2
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final SubcategoryService subcategoryService;

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

    @PostMapping("/default-image/{advertisementId}/{imageId}")
    public ResponseEntity<HttpStatus> setDefaultImage(
            @PathVariable @PositiveOrZero(message = "{invalid.id}") Long advertisementId,
            @PathVariable @PositiveOrZero(message = "{invalid.id}") Long imageId,
            Principal principal) {
        User owner = getUser(principal.getName());
        advertisementService.setDefaultImage(advertisementId, imageId, owner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateAdvertisementOwner(@RequestBody @Valid AdvertisementDto dto,
                                            User owner) {

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
