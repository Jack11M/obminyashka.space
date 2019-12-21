package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.SubcategoriesNamesDto;
import com.hillel.items_exchange.exception.InvalidCategoryVoException;
import com.hillel.items_exchange.exception.InvalidSubcategoryVoException;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

@RestController
@RequestMapping("/subcategory")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class SubcategoryController {

    private static final String NAME_OF_CLASS = "IN the SubcategoryController: ";
    private final SubcategoryService subcategoryService;

    @GetMapping("/{category_id}/names")
    public ResponseEntity<SubcategoriesNamesDto> getSubcategoryNamesByCategoryId(@PathVariable("category_id") Long id) {
        SubcategoriesNamesDto subcategoriesNames = subcategoryService.findSubcategoryNamesByCategoryId(id);
        if (subcategoriesNames.getSubcategoriesNamesDto().equals(Collections.emptyList())) {
            throw new EntityNotFoundException(StringUtils.NO_SUBCATEGORIES_BY_CATEGORY_ID + id);
        } else {
            return new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
        }
    }

    @PreAuthorize(StringUtils.HAS_ROLE_ADMIN)
    @DeleteMapping("/subcategory/{subcategory_id}")
    public ResponseEntity<HttpStatus> deleteSubcategoryById(@PathVariable("subcategory_id") Long id) {
        if (isSubcategoryDeletable(id)) {
            subcategoryService.removeSubcategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new InvalidSubcategoryVoException(StringUtils.SUBCATEGORY_CAN_NOT_BE_DELETED + id);
        }
    }

    private boolean isSubcategoryDeletable(Long subcategoryVoId) {
        if (subcategoryService.isSubcategoryVoIdValid(subcategoryVoId)) {
            if (!subcategoryService.isSubcategoryHasNotProducts(subcategoryVoId)) {
                throw new InvalidSubcategoryVoException(StringUtils.MUST_NOT_HAVE_PRODUCTS);
            }
            return true;
        } else {
            throw new InvalidSubcategoryVoException(StringUtils.MUST_EXIST_BY_ID);
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidCategoryVoException.class)
    public ResponseEntity<String> handleInvalidSubcategoryControllerDtoException(InvalidSubcategoryVoException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
