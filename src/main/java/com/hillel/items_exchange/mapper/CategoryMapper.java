package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.CategoryVo;
import com.hillel.items_exchange.dto.SubcategoryVo;
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

    public Category categoryVoToNewCategory(CategoryVo categoryVo) {
        Category newCategory = new Category();
        newCategory.setName(categoryVo.getName());
        List<Subcategory> newSubcategories = categoryVo.getSubcategories().stream()
                .map(subcategoryDto -> subcategoryMapper.subcategoryVoToNewSubcategory(subcategoryDto, newCategory))
                .collect(Collectors.toList());
        newCategory.setSubcategories(newSubcategories);
        return newCategory;
    }

    public CategoryVo categoryToCategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(category.getId());
        categoryVo.setName(category.getName());
        List<SubcategoryVo> subcategoryDtos = category.getSubcategories().stream()
                .map(subcategoryMapper::subcategoryToSubcategoryVo)
                .collect(Collectors.toList());
        categoryVo.setSubcategories(subcategoryDtos);
        return categoryVo;
    }

    public Category categoryVoToUpdatedCategory(CategoryVo categoryVo, Category updatedCategory) {
        updatedCategory.setName(categoryVo.getName());
        List<Subcategory> updatedSubcategories = categoryVo.getSubcategories().stream()
                .map(subcategoryDto -> subcategoryMapper.updateCategory(subcategoryDto, updatedCategory))
                .collect(Collectors.toList());
        updatedCategory.setSubcategories(updatedSubcategories);

        return updatedCategory;
    }
}
