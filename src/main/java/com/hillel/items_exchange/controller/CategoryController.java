package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.service.CategoryService;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final MessageSourceUtil messageSourceUtil;
    private final CategoryService categoryService;

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();
        if (categoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("category_id") long id) {
        return categoryService.findCategoryById(id)
                .map(categoryDto -> new ResponseEntity<>(categoryDto, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(messageSourceUtil.getExceptionMessageSourceWithId(
                        id, "invalid.category.id")));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (isCategoryIdValidForCreating(categoryDto)
                && categoryService.isCategoryNameHasNotDuplicate(categoryDto.getName())) {

            return new ResponseEntity<>(categoryService.addNewCategory(categoryDto), HttpStatus.CREATED);
        }

        throw new InvalidDtoException(messageSourceUtil.getExceptionMessageSource("invalid.new-category-dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        if (isDtoIdGreaterThanZero(categoryDto.getId()) && categoryService.isCategoryDtoUpdatable(categoryDto)) {
            return new ResponseEntity<>(categoryService.updateCategory(categoryDto), HttpStatus.ACCEPTED);
        }

        throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSource("invalid.updated-category.dto"));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    public ResponseEntity<CategoryDto> deleteCategoryById(@PathVariable("category_id") long id) {
        if (isDtoIdGreaterThanZero(id) && categoryService.isCategoryDtoDeletable(id)) {
            categoryService.removeCategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        throw new InvalidDtoException(messageSourceUtil.getExceptionMessageSourceWithId(id, "category.not-deletable"));
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
