package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.mapper.transfer.Exist;
import com.hillel.items_exchange.mapper.transfer.New;
import com.hillel.items_exchange.service.CategoryService;
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
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/names")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all names of existing categories.")
    public List<String> getAllCategoryNames() {
        return categoryService.findAllCategoryNames();
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all existing categories.")
    public List<CategoryDto> getAllCategories() {
        return categoryService.findAllCategoryDtos();
    }

    @GetMapping("/{category_id}")
    @ApiOperation(value = "Get a category by its ID, if it exists")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("category_id")
                                                       @Positive(message = "{invalid.exist.id}") long id) {

        return ResponseEntity.of(categoryService.findCategoryDtoById(id));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Save the new category if its and internal subcategory IDs are zero. Only for users authorized as admin.")
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
    public CategoryDto updateCategory(@Validated(Exist.class) @RequestBody CategoryDto categoryDto) {
        if (categoryService.isCategoryDtoUpdatable(categoryDto)) {
            return categoryService.saveCategoryWithSubcategories(categoryDto);
        }

        throw new IllegalIdentifierException(getExceptionMessageSource("invalid.updated-category.dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    @ApiOperation(value = "Delete category by id if it exists and internal subcategories have not products. Only for users authorized as admin.")
    public ResponseEntity<CategoryDto> deleteCategoryById(@PathVariable("category_id")
                                                          @Positive(message = "{invalid.exist.id}") long id) {

        if (categoryService.isCategoryDtoDeletable(id)) {
            categoryService.removeById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        throw new InvalidDtoException(getExceptionMessageSourceWithId(id, "category.not-deletable"));
    }
}
