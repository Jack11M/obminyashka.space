package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryControllerDto;
import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PostMapping
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> addCategory(@Valid @RequestBody CategoryControllerDto dto) {
        if (categoryService.addNewCategory(dto).isPresent()) {
            return new ResponseEntity<>(categoryService.addNewCategory(dto).get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> updateCategory(@Valid @RequestBody CategoryControllerDto dto) {
        if (categoryService.updateCategory(dto).isPresent()) {
            return new ResponseEntity<>(categoryService.updateCategory(dto).get(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
