package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.SubcategoryDto;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import com.hillel.items_exchange.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubcategoryMapper {

    private final SubcategoryService subcategoryService;

    public Subcategory dtoToNewSubcategory(SubcategoryDto subcategoryDto, Category category) {
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setName(subcategoryDto.getName());
        newSubcategory.setCategory(category);
        return newSubcategory;
    }

    public SubcategoryDto subcategoryToDto(Subcategory subcategory) {
        SubcategoryDto subcategoryDto = new SubcategoryDto();
        subcategoryDto.setId(subcategory.getId());
        subcategoryDto.setName(subcategory.getName());
        return subcategoryDto;
    }

    public Subcategory dtoToUpdatedSubcategory(SubcategoryDto subcategoryDto) {
        Subcategory updatedSubcategory = subcategoryService.findById(subcategoryDto.getId());
        updatedSubcategory.setName(subcategoryDto.getName());
        return updatedSubcategory;
    }

    public Subcategory updateSubcategory(SubcategoryDto subcategoryDto, Category category) {
        if (subcategoryDto.getId() == 0) {
            return dtoToNewSubcategory(subcategoryDto, category);
        }
        return dtoToUpdatedSubcategory(subcategoryDto);
    }
}
