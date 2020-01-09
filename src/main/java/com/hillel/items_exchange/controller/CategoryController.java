package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryVo;
import com.hillel.items_exchange.exception.InvalidVoException;
import com.hillel.items_exchange.service.CategoryService;
import com.hillel.items_exchange.util.ExceptionTextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private static final String NAME_OF_CLASS = "IN the CategoryController: ";
    private final CategoryService categoryService;

    @GetMapping("/names")
    public ResponseEntity<List<String>> allCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();
        if (isEqualsEmptyList(categoriesNames)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryVo>> getAllCategories() {
        List<CategoryVo> categories = categoryService.findAllCategories();
        if (isEqualsEmptyList(categories)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<CategoryVo> getCategoryById(@PathVariable("category_id") long id) {
        return categoryService.findCategoryById(id)
                .map(categoryVo -> new ResponseEntity<>(categoryVo, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(ExceptionTextMessage.NO_CATEGORY_BY_ID + id));
    }

    @PreAuthorize(ExceptionTextMessage.HAS_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<CategoryVo> addCategory(@Valid @RequestBody CategoryVo categoryVo) {
        if (!categoryService.isCategoryVoCreatable(categoryVo)) {
            throw new IllegalIdentifierException(ExceptionTextMessage.MUST_HAVE_ID_ZERO);
        }
        return new ResponseEntity<>(categoryService.addNewCategory(categoryVo), HttpStatus.CREATED);
    }

    @PreAuthorize(ExceptionTextMessage.HAS_ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<CategoryVo> updateCategory(@Valid @RequestBody CategoryVo categoryVo) {
        if (!categoryService.isCategoryVoUpdatable(categoryVo)) {
            throw new IllegalIdentifierException(ExceptionTextMessage.SUBCATEGORIES_MUST_EXIST_BY_ID_OR_ZERO);
        }
        return categoryService.updateCategory(categoryVo)
                .map(categoryVO -> new ResponseEntity<>(categoryVo, HttpStatus.ACCEPTED))
                .orElseThrow(() -> new InvalidVoException(ExceptionTextMessage.CAN_NOT_BE_UPDATED + categoryVo));
    }

    @PreAuthorize(ExceptionTextMessage.HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    public ResponseEntity<CategoryVo> deleteCategoryById(@PathVariable("category_id") long id) {
        if (categoryService.isCategoryVoDeletable(id)) {
            categoryService.removeCategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new InvalidVoException(ExceptionTextMessage.CATEGORY_CAN_NOT_BE_DELETED + id);
    }

    private boolean isEqualsEmptyList(List<?> categories) {
        return categories.size() == 0;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidVoException.class)
    public ResponseEntity<String> handleInvalidCategoryControllerDtoException(InvalidVoException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<String> handleIdException(IllegalIdentifierException e) {
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
