package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoryControllerDto;
import com.hillel.items_exchange.exception.InvalidCategoryControllerDtoException;
import com.hillel.items_exchange.service.CategoryService;
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
import java.util.Optional;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/names")
    public ResponseEntity<List<String>> allCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();

        if (categoriesNames.isEmpty()) {
            return new ResponseEntity<>(categoriesNames, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryControllerDto>> getAllCategories() {
        List<CategoryControllerDto> categoriesDto = categoryService.findAllCategories();

        if (!categoriesDto.isEmpty()) {
            return new ResponseEntity<>(categoryService.findAllCategories(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("No categories");
        }
    }

    @GetMapping("/{category_id}")
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> getCategoryById(@PathVariable("category_id") Long id) {
        return categoryService.findCategoryById(id)
                .map(advertisementDto -> new ResponseEntity<>(advertisementDto, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(String.format("No category by id %s", id)));
    }

    @GetMapping("/{category_id}/subcategories/names")
    public @ResponseBody
    ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@PathVariable("category_id") Long id) {
        List<String> subcategoriesNames = categoryService.findSubcategoryNamesByCategoryId(id);

        if (!subcategoriesNames.isEmpty()) {
            return new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(subcategoriesNames, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> addCategory(@Valid @RequestBody CategoryControllerDto dto) {
        Optional<CategoryControllerDto> categoryDto = categoryService.addNewCategory(dto);
        return categoryDto.map(categoryControllerDto -> new ResponseEntity<>(categoryControllerDto, HttpStatus.CREATED))
                .orElseThrow(() -> new InvalidCategoryControllerDtoException(String.format("This category controller dto " +
                        "can not be created: %s. It and its internal subcategories must not have id! The new category " +
                        "must not have a name like the existing category", dto)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public @ResponseBody
    ResponseEntity<CategoryControllerDto> updateCategory(@Valid @RequestBody CategoryControllerDto dto) {
        String errorMessage = String.format("This category controller dto " +
                "can not be updated: %s. It has to have existing id and its internal subcategories has to have id, " +
                "if they were exist before or can have no id, if they are new! Also the updated category must not " +
                "have a name like the existing category", dto);

        Optional<CategoryControllerDto> categoryDto = Optional.ofNullable(categoryService.updateCategory(dto)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage)));
        return categoryDto.map(categoryControllerDto -> new ResponseEntity<>(categoryControllerDto, HttpStatus.ACCEPTED))
                .orElseThrow(() -> new InvalidCategoryControllerDtoException(errorMessage));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{category_id}")
    public ResponseEntity<CategoryControllerDto> deleteCategoryById(@PathVariable("category_id") Long id) {
        if (categoryService.removeCategoryById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new IllegalIdentifierException(String.format("The category with id: %s can not be deleted. " +
                    "The id has to be valid (represent existing category) and internal subcategories must not have " +
                    "any products!", id));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/subcategory/{subcategory_id}")
    public ResponseEntity<HttpStatus> deleteSubcategoryById(@PathVariable("subcategory_id") Long id) {
        if (categoryService.removeSubcategoryById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new IllegalIdentifierException(String.format("The subcategory with id: %s can not be deleted." +
                    " The id has to be valid (represent existing subcategory) and this subcategory must not have " +
                    "any products!", id));
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidCategoryControllerDtoException.class)
    public ResponseEntity<String> handleInvalidCategoryControllerDtoException(InvalidCategoryControllerDtoException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<String> handleIllegalIdException(IllegalIdentifierException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
