package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.dto.SubcategoryDto;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final SubcategoryMapper subcategoryMapper;

    public Category dtoToNewCategory(CategoryDto categoryDto) {
        Category newCategory = new Category();
        return fillCategoryWithSubcategories(categoryDto, newCategory);
    }

    public CategoryDto categoryToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        List<SubcategoryDto> subcategoryDtos = category.getSubcategories().stream()
                .map(subcategoryMapper::subcategoryToDto)
                .collect(Collectors.toList());
        categoryDto.setSubcategories(subcategoryDtos);
        return categoryDto;
    }

    public Category dtoToUpdatedCategory(CategoryDto categoryDto, Category updatedCategory) {
        return fillCategoryWithSubcategories(categoryDto, updatedCategory);
    }

    private Category fillCategoryWithSubcategories(CategoryDto categoryDto, Category category) {
        category.setName(categoryDto.getName());
        List<Subcategory> subcategories = categoryDto.getSubcategories().stream()
                .map(subcategoryDto -> subcategoryMapper.updateSubcategory(subcategoryDto, category))
                .collect(Collectors.toList());
        category.setSubcategories(subcategories);
        return category;
    }
}
