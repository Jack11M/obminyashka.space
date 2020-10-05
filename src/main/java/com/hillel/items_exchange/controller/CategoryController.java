package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping("/category")
@Api(tags = "Category")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/names")
    @ApiOperation(value = "Find all categories' name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<String>> getAllCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();
        if (categoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all categories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    @ApiOperation(value = "Find a category its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<CategoryDto> getCategoryById(@PositiveOrZero(message = "{invalid.id}")
                                                           @PathVariable("category_id") long id) {
        return categoryService.findCategoryById(id)
                .map(categoryDto -> new ResponseEntity<>(categoryDto, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(getExceptionMessageSourceWithId(
                        id, "invalid.category.id")));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    @ApiOperation(value = "Create a new category. (ADMIN ONLY)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (isCategoryIdValidForCreating(categoryDto)
                && categoryService.isCategoryNameHasNotDuplicate(categoryDto.getName())) {

            return new ResponseEntity<>(categoryService.addNewCategory(categoryDto), HttpStatus.CREATED);
        }

        throw new InvalidDtoException(getExceptionMessageSource("invalid.new-category-dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping
    @ApiOperation(value = "Update an existed category. (ADMIN ONLY)")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (isDtoIdGreaterThanZero(categoryDto.getId()) && categoryService.isCategoryDtoUpdatable(categoryDto)) {
            return new ResponseEntity<>(categoryService.updateCategory(categoryDto), HttpStatus.ACCEPTED);
        }

        throw new IllegalIdentifierException(getExceptionMessageSource("invalid.updated-category.dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    @ApiOperation(value = "Delete an existed category. (ADMIN ONLY)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<CategoryDto> deleteCategoryById(@PositiveOrZero(message = "{invalid.id}")
                                                              @PathVariable("category_id") long id) {
        if (isDtoIdGreaterThanZero(id) && categoryService.isCategoryDtoDeletable(id)) {
            categoryService.removeCategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        throw new InvalidDtoException(getExceptionMessageSourceWithId(id, "category.not-deletable"));
    }

    private boolean isCategoryIdValidForCreating(CategoryDto categoryDto) {
        return isIdEqualsZero(categoryDto.getId())
                && categoryDto.getSubcategories().stream()
                .allMatch(subcategoryDto -> isIdEqualsZero(subcategoryDto.getId()));
    }

    private boolean isDtoIdGreaterThanZero(long id) {
        return id > 0;
    }

    private boolean isIdEqualsZero(long id) {
        return id == 0;
    }

}
