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

    public Optional<CategoryDto> findCategoryById(long id) {
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

    public void removeCategoryById(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public boolean isCategoryExistsById(long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public boolean isCategoryDtoDeletable(long categoryId) {
        return isCategoryExistsById(categoryId) && isInternalSubcategoriesHaveNotProducts(categoryId);
    }

    public boolean isCategoryDtoUpdatable(CategoryDto categoryDto) {
        return isCategoryExistsById(categoryDto.getId())
                && isCategoryNameHasNotDuplicateExceptCurrentName(categoryDto)
                && isSubcategoryHasIdZeroOrIdExists(categoryDto);
    }

    private boolean isSubcategoryHasIdZeroOrIdExists(CategoryDto categoryDto) {
        return categoryDto.getSubcategories().stream()
                .filter(subcategoryDto -> subcategoryDto.getId() != 0)
                .allMatch(subcategoryDto -> subcategoryService.isSubcategoryExistsById(subcategoryDto.getId()));
    }

    public boolean isCategoryNameHasNotDuplicate(String name) {
        boolean categoryNamesHasDuplicate = findAllCategoryNames().stream()
                .anyMatch(categoryName -> categoryName.equalsIgnoreCase(name));

        return !categoryNamesHasDuplicate;
    }

    public boolean isCategoryNameHasNotDuplicateExceptCurrentName(CategoryDto categoryDto) {
        Category category = categoryRepository.findCategoryById(categoryDto
                .getId());

        return findAllCategories().stream()
                .filter(dto -> !dto.getName().equals(category.getName()))
                .map(CategoryDto::getName)
                .noneMatch(categoryName -> categoryName.equals(categoryDto.getName()));
    }

    private boolean isInternalSubcategoriesHaveNotProducts(long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(c -> c.getSubcategories().stream()
                        .map(Subcategory::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()).stream()
                .allMatch(subcategoryService::isSubcategoryHasNotProducts);
    }

    private CategoryDto mapCategoryToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
