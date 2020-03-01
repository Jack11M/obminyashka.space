package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/adv")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Log4j2
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final SubcategoryService subcategoryService;

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
    ResponseEntity<List<AdvertisementDto>> getAllBySearchParameters(@Valid @RequestBody AdvertisementFilterDto advertisementFilterDto) {
        return new ResponseEntity<>(advertisementService.findAdvertisementsByMultipleParams(advertisementFilterDto), HttpStatus.OK);
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<AdvertisementDto> createAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {
        long id = dto.getId();
        long subcategoryId = dto.getProduct().getSubcategoryId();
        boolean subcategoryIdExists = subcategoryService.existById(subcategoryId);

        if (id != 0) {
            throw new IllegalIdentifierException("New advertisement hasn't contain any id but it was received: " + id);
        }

        if (subcategoryId == 0 || !subcategoryIdExists) {
            throw new IllegalIdentifierException("Subcategory has to exist by id, but it does not with this id: " + id);
        }
        return new ResponseEntity<>(advertisementService.createAdvertisement(dto, getUser(principal.getName())), HttpStatus.CREATED);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<AdvertisementDto> updateAdvertisement(@Valid @RequestBody AdvertisementDto dto, Principal principal) {
        User owner = getUser(principal.getName());
        validateAdvertisementOwner(dto, owner);
        return new ResponseEntity<>(advertisementService.updateAdvertisement(dto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
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

    private void validateAdvertisementOwner(@RequestBody @Valid AdvertisementDto dto, User owner) {
        if (!advertisementService.isAdvertisementExists(dto.getId(), owner)) {
            throw new SecurityException("User: " + owner + " don't own gained advertisement: " + dto);
        }
    }

    private User getUser(String userNameOrEmail) {
        return userService.findByUsernameOrEmail(userNameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + userNameOrEmail + " not found!"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsernameNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User with such login or e-mail not found!");
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleOwnerException(SecurityException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Current user doesn't own gained advertisement!");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleOwnerException(EntityNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Entity not found into the database!\n" + e.getLocalizedMessage());
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<String> handleIdException(IllegalIdentifierException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("New advertisement does't have to contain id except 0!");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSqlException(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Exception during saving object to the database!\nError message:\n" + e.getLocalizedMessage());
    }
}
