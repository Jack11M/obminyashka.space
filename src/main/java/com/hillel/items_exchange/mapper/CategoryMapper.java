package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.dto.CategoryControllerDto;
import com.hillel.items_exchange.dto.SubcategoryControllerDto;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final SubcategoryRepository subcategoryRepository;

    public Category categoryControllerDtoToNewCategory(CategoryControllerDto categoryControllerDto) {
        Category newCategory = new Category();
        newCategory.setName(categoryControllerDto.getName());
        List<Subcategory> newSubcategories = categoryControllerDto.getSubcategories().stream()
                .map(subcategoryDto -> subcategoryControllerDtoToNewSubcategory(subcategoryDto, newCategory))
                .collect(Collectors.toList());
        newCategory.setSubcategories(newSubcategories);

        return newCategory;
    }

    public CategoryControllerDto categoryToCategoryDto(Category category) {
        CategoryControllerDto categoryControllerDto = new CategoryControllerDto();
        categoryControllerDto.setId(category.getId());
        categoryControllerDto.setName(category.getName());
        List<SubcategoryControllerDto> subcategoryDtos = category.getSubcategories().stream()
                .map(this::subcategoryToSubcategoryDto)
                .collect(Collectors.toList());
        categoryControllerDto.setSubcategories(subcategoryDtos);
        return categoryControllerDto;
    }

    public Category categoryControllerDtoToUpdatedCategory(CategoryControllerDto categoryControllerDto, Category updatedCategory) {

        updatedCategory.setName(categoryControllerDto.getName());
        List<Subcategory> updatedSubcategories = categoryControllerDto.getSubcategories().stream()
                .map(subcategoryDto -> updateCategory(subcategoryDto, updatedCategory))
                .collect(Collectors.toList());
        updatedCategory.setSubcategories(updatedSubcategories);

        return updatedCategory;
    }


    private Subcategory subcategoryControllerDtoToNewSubcategory(SubcategoryControllerDto subcategoryControllerDto,
                                                                 Category category) {
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setName(subcategoryControllerDto.getName());
        newSubcategory.setCategory(category);
        return newSubcategory;
    }

    private SubcategoryControllerDto subcategoryToSubcategoryDto(Subcategory subcategory) {
        SubcategoryControllerDto subcategoryControllerDto = new SubcategoryControllerDto();
        subcategoryControllerDto.setId(subcategory.getId());
        subcategoryControllerDto.setName(subcategory.getName());
        return subcategoryControllerDto;
    }

    private Subcategory subcategoryControllerDtoToUpdatedSubcategory(SubcategoryControllerDto subcategoryControllerDto) {
        Optional<Subcategory> updatedSubcategory = subcategoryRepository.findById(subcategoryControllerDto.getId());
        updatedSubcategory.ifPresent(subcategory -> subcategory.setName(subcategoryControllerDto.getName()));
        return updatedSubcategory.get();
    }

    private Subcategory updateCategory(SubcategoryControllerDto subcategoryDto, Category category) {
        if (subcategoryDto.getId() == null) {
            return subcategoryControllerDtoToNewSubcategory(subcategoryDto, category);
        } else {
            return subcategoryControllerDtoToUpdatedSubcategory(subcategoryDto);
        }
    }
}
