package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.mapper.tranfer.Exist;
import com.hillel.items_exchange.mapper.tranfer.New;
import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/names")
    @ApiOperation(value = "Get all names of existing categories.")
    public ResponseEntity<List<String>> getAllCategoryNames() {
        return new ResponseEntity<>(categoryService.findAllCategoryNames(), HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all existing categories.")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(categoryService.findAllCategoryDto(), HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    @ApiOperation(value = "Get a category by its ID, if it exists")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("category_id") long id) {
        return categoryService.findCategoryDtoById(id)
                .map(categoryDto -> new ResponseEntity<>(categoryDto, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(getExceptionMessageSourceWithId(
                        id, "invalid.category.id")));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    @ApiOperation(value = "Save the new category if its and internal subcategory IDs are zero. Only for users authorized as admin.")
    public ResponseEntity<CategoryDto> createCategory(@Validated(New.class) @RequestBody CategoryDto categoryDto) {
        if (categoryService.isCategoryDtoValidForCreating(categoryDto)) {
            return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
        }

        throw new InvalidDtoException(getExceptionMessageSource("invalid.new-category-dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping
    @ApiOperation(value = "Update an existent category. Only for users authorized as admin.")
    public ResponseEntity<CategoryDto> updateCategory(@Validated(Exist.class) @RequestBody CategoryDto categoryDto) {
        if (categoryService.isCategoryDtoUpdatable(categoryDto)) {
            return new ResponseEntity<>(categoryService.update(categoryDto), HttpStatus.ACCEPTED);
        }

        throw new IllegalIdentifierException(getExceptionMessageSource("invalid.updated-category.dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    @ApiOperation(value = "Delete category by id if it exists and internal subcategories have not products. Only for users authorized as admin.")
    public ResponseEntity<CategoryDto> deleteCategoryById(@Validated(Exist.class) @PathVariable("category_id") long id) {
        if (categoryService.isCategoryDtoDeletable(id)) {
            categoryService.removeById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        throw new InvalidDtoException(getExceptionMessageSourceWithId(id, "category.not-deletable"));
    }
}
