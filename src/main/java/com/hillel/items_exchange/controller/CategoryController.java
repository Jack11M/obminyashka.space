package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.mapper.transfer.Exist;
import com.hillel.items_exchange.mapper.transfer.New;
import com.hillel.items_exchange.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping("/category")
@Api(tags = "Category")
@Validated
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all names of existing categories.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<String>> getAllCategoryNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();
        if (categoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all existing categories.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAllCategoryDtos();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    @ApiOperation(value = "Get a category by its ID, if it exists")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("category_id")
                                                       @Positive(message = "{invalid.exist.id}") long id) {

        return ResponseEntity.of(categoryService.findCategoryDtoById(id));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Save the new category if its and internal subcategory IDs are zero. Only for users authorized as admin.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public CategoryDto createCategory(@Validated(New.class) @RequestBody CategoryDto categoryDto) {
        if (categoryService.isCategoryDtoValidForCreating(categoryDto)) {
            return categoryService.saveCategoryWithSubcategories(categoryDto);
        }

        throw new InvalidDtoException(getExceptionMessageSource("invalid.new-category-dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Update an existent category. Only for users authorized as admin.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public CategoryDto updateCategory(@Validated(Exist.class) @RequestBody CategoryDto categoryDto) {
        if (categoryService.isCategoryDtoUpdatable(categoryDto)) {
            return categoryService.saveCategoryWithSubcategories(categoryDto);
        }

        throw new IllegalIdentifierException(getExceptionMessageSource("invalid.updated-category.dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    @ApiOperation(value = "Delete category by id if it exists and internal subcategories have not products. Only for users authorized as admin.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<CategoryDto> deleteCategoryById(@PathVariable("category_id")
                                                          @Positive(message = "{invalid.exist.id}") long id) {

        if (categoryService.isCategoryDtoDeletable(id)) {
            categoryService.removeById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        throw new InvalidDtoException(getExceptionMessageSourceWithId(id, "category.not-deletable"));
    }
}
