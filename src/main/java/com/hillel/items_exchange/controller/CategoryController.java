package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.CategoriesNamesDto;
import com.hillel.items_exchange.dto.CategoriesVo;
import com.hillel.items_exchange.dto.CategoryVo;
import com.hillel.items_exchange.exception.InvalidCategoryVoException;
import com.hillel.items_exchange.service.CategoryService;
import com.hillel.items_exchange.service.SubcategoryService;
import com.hillel.items_exchange.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private static final String NAME_OF_CLASS = "IN the CategoryController: ";
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;

    @GetMapping("/names")
    public ResponseEntity<CategoriesNamesDto> allCategoriesNames() {
        CategoriesNamesDto categoriesNames = categoryService.findAllCategoryNames();
        if (categoriesNames == null || categoriesNames.getCategoriesNames().equals(Collections.emptyList())) {
            throw new EntityNotFoundException(StringUtils.NO_CATEGORIES);
        }
        return new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CategoriesVo> getAllCategories() {
        Optional<CategoriesVo> categoriesVo = categoryService.findAllCategories();
        return categoriesVo.map(c -> new ResponseEntity<>(categoriesVo.get(), HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(StringUtils.NO_CATEGORIES));
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<CategoryVo> getCategoryById(@PathVariable("category_id") Long id) {
        return categoryService.findCategoryById(id)
                .map(categoryVo -> new ResponseEntity<>(categoryVo, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException(StringUtils.NO_CATEGORY_BY_ID + id));
    }

    @PreAuthorize(StringUtils.HAS_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<CategoryVo> addCategory(@Valid @RequestBody CategoryVo categoryVo) {
        Optional<CategoryVo> updatedCategoryVo = Optional.empty();
        if (isCategoryVoCreatable(categoryVo)) {
            updatedCategoryVo = categoryService.addNewCategory(categoryVo);
        }
        return updatedCategoryVo.map(categoryVO -> new ResponseEntity<>(categoryVO, HttpStatus.CREATED))
                .orElseThrow(() -> new InvalidCategoryVoException(StringUtils.CAN_NOT_BE_CREATED + categoryVo));
    }

    @PreAuthorize(StringUtils.HAS_ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<CategoryVo> updateCategory(@Valid @RequestBody CategoryVo categoryVo) {
        if (isCategoryVoUpdatable(categoryVo)) {
            return categoryService.updateCategory(categoryVo)
                    .map(categoryVO -> new ResponseEntity<>(categoryVo, HttpStatus.ACCEPTED))
                    .orElseThrow(() -> new InvalidCategoryVoException(StringUtils.CAN_NOT_BE_UPDATED + categoryVo));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize(StringUtils.HAS_ROLE_ADMIN)
    @DeleteMapping("/{category_id}")
    public ResponseEntity<CategoryVo> deleteCategoryById(@PathVariable("category_id") Long id) {
        if (isCategoryVoDeletable(id)) {
            categoryService.removeCategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new InvalidCategoryVoException(StringUtils.CAN_NOT_BE_DELETED + id);
        }
    }

    private boolean isCategoryVoCreatable(CategoryVo categoryVo) {
        boolean isCategoryVoNotDistinct = categoryService.findAllCategoryNames().getCategoriesNames()
                .contains(categoryVo.getName());
        boolean isSubcategoriesIdIsZero = categoryVo.getSubcategories().stream()
                .allMatch(subcategoryVo -> subcategoryVo.getId() == 0);

        if (categoryVo.getId() != 0) {
            throw new InvalidCategoryVoException(StringUtils.MUST_HAVE_ID_ZERO);
        }
        if (!isSubcategoriesIdIsZero) {
            throw new InvalidCategoryVoException(StringUtils.ID_ZERO_OF_ALL_SUBCATEGORIES);
        }
        if (isCategoryVoNotDistinct) {
            throw new InvalidCategoryVoException(StringUtils.CATEGORY_MUST_BE_DISTINCT + categoryVo.getName());
        }
        return true;
    }

    private boolean isCategoryVoUpdatable(CategoryVo categoryVo) {
        if (categoryService.isCategoryVoIdInvalid(categoryVo.getId())) {
            throw new InvalidCategoryVoException(StringUtils.CATEGORY_MUST_EXIST_BY_ID);
        }
        if (subcategoryService.isSubcategoriesVoIdsInvalid(categoryVo.getSubcategories())) {
            throw new InvalidCategoryVoException(StringUtils.SUBCATEGORIES_MUST_EXIST_BY_ID_OR_ZERO);
        }
        return true;
    }

    private boolean isCategoryVoDeletable(Long categoryId) {
        if (categoryService.isCategoryVoIdInvalid(categoryId)) {
            throw new InvalidCategoryVoException(StringUtils.CATEGORY_MUST_EXIST_BY_ID);
        } else {
            List<Long> subcategoriesIdList = categoryService.getSubcategoriesIdsByCategoryId(categoryId);
            boolean isSubcategoriesIdsValid = subcategoriesIdList.stream()
                    .allMatch(id -> subcategoryService.isSubcategoryVoIdValid(id)
                            && subcategoryService.isSubcategoryHasNotProducts(id));

            return !subcategoriesIdList.isEmpty() && isSubcategoriesIdsValid;
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidCategoryVoException.class)
    public ResponseEntity<String> handleInvalidCategoryControllerDtoException(InvalidCategoryVoException e) {
        log.warn(NAME_OF_CLASS + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
