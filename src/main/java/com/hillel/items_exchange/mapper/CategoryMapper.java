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

    public Category voToNewCategory(CategoryVo categoryVo) {
        Category newCategory = new Category();
        return fillCategoryWithSubcategories(categoryVo, newCategory);
    }

    public CategoryVo categoryToVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(category.getId());
        categoryVo.setName(category.getName());
        List<SubcategoryVo> subcategoryDtos = category.getSubcategories().stream()
                .map(subcategoryMapper::subcategoryToVo)
                .collect(Collectors.toList());
        categoryVo.setSubcategories(subcategoryDtos);
        return categoryVo;
    }

    public Category voToUpdatedCategory(CategoryVo categoryVo, Category updatedCategory) {
        return fillCategoryWithSubcategories(categoryVo, updatedCategory);
    }

    private Category fillCategoryWithSubcategories(CategoryVo categoryVo, Category category) {
        category.setName(categoryVo.getName());
        List<Subcategory> subcategories = categoryVo.getSubcategories().stream()
                .map(subcategoryDto -> subcategoryMapper.updateSubcategory(subcategoryDto, category))
                .collect(Collectors.toList());
        category.setSubcategories(subcategories);
        return category;
    }
}
