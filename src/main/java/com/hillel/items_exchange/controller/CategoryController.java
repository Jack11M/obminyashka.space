package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryControllerDto;
import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/names")
    public ResponseEntity<List<String>> allCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();

        if (categoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryControllerDto>> getAllCategories() {
        List<CategoryControllerDto> categoriesDto = categoryService.findAllCategories();

        if (categoriesDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(categoryService.findAllCategories(), HttpStatus.OK);
        }
    }

    @GetMapping("/{category_id}")
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> getCategoryById(@PathVariable("category_id") Long id) {
        return categoryService.findCategoryById(id)
                .map(advertisementDto -> new ResponseEntity<>(advertisementDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{category_id}/subcategories/names")
    public @ResponseBody
    ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@PathVariable("category_id") Long id) {
        List<String> subcategoriesNames = categoryService.findSubcategoryNamesByCategoryId(id);

        if (subcategoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> addCategory(@Valid @RequestBody CategoryControllerDto dto) {
        Optional<CategoryControllerDto> categoryDto = categoryService.addNewCategory(dto);
        return categoryDto.map(categoryControllerDto -> new ResponseEntity<>(categoryControllerDto, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> updateCategory(@Valid @RequestBody CategoryControllerDto dto) {
        Optional<CategoryControllerDto> categoryDto = categoryService.updateCategory(dto);
        return categoryDto.map(categoryControllerDto -> new ResponseEntity<>(categoryControllerDto, HttpStatus.ACCEPTED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{category_id}")
    public ResponseEntity<CategoryControllerDto> deleteCategoryById(@PathVariable("category_id") Long id) {
        if (categoryService.removeCategoryById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/subcategory/{subcategory_id}")
    public ResponseEntity<HttpStatus> deleteSubcategoryById(@PathVariable("subcategory_id") Long id) {
        if (categoryService.removeSubcategoryById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
