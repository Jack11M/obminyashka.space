package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.exception.InvalidVoException;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.util.ExceptionTextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/subcategory")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class SubcategoryController {

    private static final String NAME_OF_CLASS = "IN the SubcategoryController: ";
    private final SubcategoryService subcategoryService;

    @GetMapping("/{category_id}/names")
    public ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@PathVariable("category_id") long id) {
        List<String> subcategoriesNames = subcategoryService.findSubcategoryNamesByCategoryId(id);
        if (subcategoriesNames.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
    }

    @PreAuthorize(ExceptionTextMessage.HAS_ROLE_ADMIN)
    @DeleteMapping("/{subcategory_id}")
    public ResponseEntity<HttpStatus> deleteSubcategoryById(@PathVariable("subcategory_id") long id) {
        if (!subcategoryService.isSubcategoryDeletable(id)) {
            throw new InvalidVoException(ExceptionTextMessage.SUBCATEGORY_CAN_NOT_BE_DELETED + id);
        }
        subcategoryService.removeSubcategoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(InvalidVoException.class)
    public ResponseEntity<String> handleInvalidSubcategoryControllerDtoException(InvalidVoException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSqlException(SQLIntegrityConstraintViolationException e) {
        log.error(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionTextMessage.SQL_EXCEPTION + e.getLocalizedMessage());
    }
}
