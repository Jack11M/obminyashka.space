package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.SubcategoryVo;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import com.hillel.items_exchange.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubcategoryMapper {

    private final SubcategoryService subcategoryService;

    public Subcategory voToNewSubcategory(SubcategoryVo subcategoryVo,
                                          Category category) {
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setName(subcategoryVo.getName());
        newSubcategory.setCategory(category);
        return newSubcategory;
    }

    public SubcategoryVo subcategoryToVo(Subcategory subcategory) {
        SubcategoryVo subcategoryVo = new SubcategoryVo();
        subcategoryVo.setId(subcategory.getId());
        subcategoryVo.setName(subcategory.getName());
        return subcategoryVo;
    }

    public Subcategory voToUpdatedSubcategory(SubcategoryVo subcategoryVo) {
        Subcategory updatedSubcategory = subcategoryService.findById(subcategoryVo.getId());
        updatedSubcategory.setName(subcategoryVo.getName());
        return updatedSubcategory;
    }

    public Subcategory updateSubcategory(SubcategoryVo subcategoryVo, Category category) {
        if (subcategoryVo.getId() == 0) {
            return voToNewSubcategory(subcategoryVo, category);
        }
        return voToUpdatedSubcategory(subcategoryVo);
    }
}
