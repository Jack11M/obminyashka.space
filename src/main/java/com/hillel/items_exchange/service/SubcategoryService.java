package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return isSubcategoryVoIdValid(id)
                && isSubcategoryHasNotProducts(id);
    }

    public boolean isSubcategoryHasNotProducts(long id) {
        return subcategoryRepository.findById(id).get().getProducts().isEmpty();
    }

    public Optional<Subcategory> findById(long id) {
        return subcategoryRepository.findById(id);
    }

    public boolean isSubcategoryVoIdValid(long id) {
        return subcategoryRepository.existsById(id);
    }
}
