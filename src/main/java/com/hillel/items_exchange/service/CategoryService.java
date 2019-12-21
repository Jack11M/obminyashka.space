package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.dto.CategoriesNamesDto;
import com.hillel.items_exchange.dto.CategoriesVo;
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

    public CategoriesNamesDto findAllCategoryNames() {
        CategoriesNamesDto categoriesNamesDto = new CategoriesNamesDto();
        categoriesNamesDto.setCategoriesNames(Optional.ofNullable(categoryRepository.findAllCategoriesNames())
                .orElse(Collections.emptyList()));
        return categoriesNamesDto;
    }

    public Optional<CategoriesVo> findAllCategories() {
        CategoriesVo categoriesVo = new CategoriesVo();
        categoriesVo.setCategoriesVo(categoryRepository.findAll()
                .stream()
                .map(this::mapCategoryToVo).collect(Collectors.toList()));

        return Optional.of(categoriesVo);
    }

    public Optional<CategoryVo> findCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapCategoryToVo);
    }

    public Optional<CategoryVo> addNewCategory(CategoryVo categoryVO) {
        return Optional.ofNullable(categoryMapper.categoryToCategoryVo(categoryRepository
                .save(categoryMapper.categoryVoToNewCategory(categoryVO))));
    }

    public Optional<CategoryVo> updateCategory(CategoryVo categoryVO) {
        Optional<Category> updatedCategory = categoryRepository.findById(categoryVO.getId());
        return updatedCategory.map(category -> mapCategoryToVo(categoryRepository
                .save(categoryMapper.categoryVoToUpdatedCategory(categoryVO, updatedCategory.get()))));
    }

    public void removeCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public boolean isCategoryVoIdInvalid(Long categoryId) {
        return categoryId <= 0 || !categoryRepository.existsById(categoryId);
    }

    public List<Long> getSubcategoriesIdsByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).map(c -> c.getSubcategories().stream()
                .map(Subcategory::getId)
                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private CategoryVo mapCategoryToVo(Category category) {
        return modelMapper.map(category, CategoryVo.class);
    }
}
