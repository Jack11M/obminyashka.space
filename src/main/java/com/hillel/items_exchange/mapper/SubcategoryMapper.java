package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.dto.SubcategoryVo;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class SubcategoryMapper {
    private final SubcategoryRepository subcategoryRepository;

    public Subcategory subcategoryVoToNewSubcategory(SubcategoryVo subcategoryVo,
                                                     Category category) {
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setName(subcategoryVo.getName());
        newSubcategory.setCategory(category);
        return newSubcategory;
    }

    public SubcategoryVo subcategoryToSubcategoryVo(Subcategory subcategory) {
        SubcategoryVo subcategoryVo = new SubcategoryVo();
        subcategoryVo.setId(subcategory.getId());
        subcategoryVo.setName(subcategory.getName());
        return subcategoryVo;
    }

    public Subcategory subcategoryVoToUpdatedSubcategory(SubcategoryVo subcategoryVo) {
        Subcategory updatedSubcategory = subcategoryRepository.findById(subcategoryVo.getId())
                .orElseThrow(EntityNotFoundException::new);
        updatedSubcategory.setName(subcategoryVo.getName());
        return updatedSubcategory;
    }

    public Subcategory updateCategory(SubcategoryVo subcategoryVo, Category category) {
        if (subcategoryVo.getId() == 0) {
            return subcategoryVoToNewSubcategory(subcategoryVo, category);
        } else {
            return subcategoryVoToUpdatedSubcategory(subcategoryVo);
        }
    }
}
