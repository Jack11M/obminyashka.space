package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;

    public void removeSubcategoryById(long subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }

    public List<String> findSubcategoryNamesByCategoryId(long categoryId) {
        return subcategoryRepository.findSubcategoriesNamesByCategory(categoryId);
    }

    public boolean isSubcategoryDeletable(long id) {
        return isSubcategoryDtoIdValid(id)
                && isSubcategoryHasNotProducts(id);
    }

    public boolean isSubcategoryHasNotProducts(long id) {
        return subcategoryRepository.findById(id).getProducts().isEmpty();
    }

    public Subcategory findById(long id) {
        return subcategoryRepository.findById(id);
    }

    public boolean isSubcategoryDtoIdValid(long id) {
        return subcategoryRepository.existsById(id);
    }

    public boolean existById(long id) {
        return subcategoryRepository.existsById(id);
    }
}
