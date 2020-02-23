package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.dto.CategoryVo;
import com.hillel.items_exchange.mapper.CategoryMapper;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;
    private final SubcategoryService subcategoryService;

    public List<String> findAllCategoryNames() {
        return categoryRepository.findAllCategoriesNames();
    }

    public List<CategoryVo> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapCategoryToVo).collect(Collectors.toList());
    }

    public Optional<CategoryVo> findCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapCategoryToVo);
    }

    public CategoryVo addNewCategory(CategoryVo categoryVO) {
        return categoryMapper.categoryToVo(categoryRepository
                .save(categoryMapper.voToNewCategory(categoryVO)));
    }

    public CategoryVo updateCategory(CategoryVo categoryVO) {
        Category updatedCategory = categoryRepository.findCategoryById(categoryVO.getId());
        return mapCategoryToVo(categoryRepository.save(categoryMapper.voToUpdatedCategory(categoryVO, updatedCategory)));
    }

    public void removeCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public boolean isCategoryVoIdValid(Long categoryId) {
        return categoryId > 0 && categoryRepository.existsById(categoryId);
    }

    public boolean isCategoryVoDeletable(long categoryId) {
        return isCategoryVoIdValid(categoryId) && categoryRepository.findById(categoryId)
                .map(c -> c.getSubcategories().stream()
                        .map(Subcategory::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()).stream()
                .allMatch(subcategoryService::isSubcategoryHasNotProducts);
    }

    public boolean isCategoryVoCreatable(CategoryVo categoryVo) {
        return isCategoryNameHasNotDuplicate(categoryVo.getName()) && (categoryVo.getId() == 0
                && categoryVo.getSubcategories().stream()
                .allMatch(subcategoryVo -> subcategoryVo.getId() == 0));
    }

    public boolean isCategoryVoUpdatable(CategoryVo categoryVo) {
        boolean isSubcategoryHasIdZeroOrIdExists = categoryVo.getSubcategories().stream()
                .filter(subcategoryDto -> subcategoryDto.getId() != 0)
                .allMatch(subcategoryVo -> subcategoryService.isSubcategoryVoIdValid(subcategoryVo.getId()));
        return isCategoryVoIdValid(categoryVo.getId()) && isCategoryNameHasNotDuplicateExceptCurrentName(categoryVo)
                && isSubcategoryHasIdZeroOrIdExists;
    }

    public boolean isCategoryNameHasNotDuplicate(String name) {
        return !findAllCategoryNames().contains(name);
    }

    public boolean isCategoryNameHasNotDuplicateExceptCurrentName(CategoryVo categoryVo) {
        return findAllCategories().stream()
                .filter(category -> !category.getName().equals(categoryRepository.findCategoryById(categoryVo.getId()).getName()))
                .map(CategoryVo::getName)
                .noneMatch(categoryName -> categoryName.equals(categoryVo.getName()));
    }

    private CategoryVo mapCategoryToVo(Category category) {
        return modelMapper.map(category, CategoryVo.class);
    }
}
