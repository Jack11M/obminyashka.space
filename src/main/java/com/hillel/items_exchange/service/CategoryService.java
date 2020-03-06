package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.dto.CategoryDto;
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

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapCategoryToDto).collect(Collectors.toList());
    }

    public Optional<CategoryDto> findCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapCategoryToDto);
    }

    public CategoryDto addNewCategory(CategoryDto categoryDto) {
        return categoryMapper.categoryToDto(categoryRepository
                .save(categoryMapper.dtoToNewCategory(categoryDto)));
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category updatedCategory = categoryRepository.findCategoryById(categoryDto.getId());
        return mapCategoryToDto(categoryRepository.save(categoryMapper.dtoToUpdatedCategory(categoryDto, updatedCategory)));
    }

    public void removeCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public boolean isCategoryDtoIdValid(Long categoryId) {
        return categoryId > 0 && categoryRepository.existsById(categoryId);
    }

    public boolean isCategoryDtoDeletable(long categoryId) {
        return isCategoryDtoIdValid(categoryId) && categoryRepository.findById(categoryId)
                .map(c -> c.getSubcategories().stream()
                        .map(Subcategory::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()).stream()
                .allMatch(subcategoryService::isSubcategoryHasNotProducts);
    }

    public boolean isCategoryDtoCreatable(CategoryDto categoryDto) {
        return isCategoryNameHasNotDuplicate(categoryDto.getName()) && (categoryDto.getId() == 0
                && categoryDto.getSubcategories().stream()
                .allMatch(subcategoryDto -> subcategoryDto.getId() == 0));
    }

    public boolean isCategoryDtoUpdatable(CategoryDto categoryDto) {
        boolean isSubcategoryHasIdZeroOrIdExists = categoryDto.getSubcategories().stream()
                .filter(subcategoryDto -> subcategoryDto.getId() != 0)
                .allMatch(subcategoryDto -> subcategoryService.isSubcategoryDtoIdValid(subcategoryDto.getId()));
        return isCategoryDtoIdValid(categoryDto.getId()) && isCategoryNameHasNotDuplicateExceptCurrentName(categoryDto)
                && isSubcategoryHasIdZeroOrIdExists;
    }

    public boolean isCategoryNameHasNotDuplicate(String name) {
        return !findAllCategoryNames().contains(name);
    }

    public boolean isCategoryNameHasNotDuplicateExceptCurrentName(CategoryDto categoryDto) {
        return findAllCategories().stream()
                .filter(category -> !category.getName().equals(categoryRepository.findCategoryById(categoryDto.getId()).getName()))
                .map(CategoryDto::getName)
                .noneMatch(categoryName -> categoryName.equals(categoryDto.getName()));
    }

    private CategoryDto mapCategoryToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
