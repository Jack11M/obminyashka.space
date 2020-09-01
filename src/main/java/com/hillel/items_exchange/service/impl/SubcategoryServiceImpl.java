package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.model.Subcategory;
import com.hillel.items_exchange.service.SubcategoryService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;

    @Override
    public List<String> findSubcategoryNamesByCategoryId(long categoryId) {
        return subcategoryRepository.findSubcategoriesNamesByCategory(categoryId);
    }

    @Override
    public void removeSubcategoryById(long subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }

    @Override
    public Optional<Subcategory> findById(long id) {
        return subcategoryRepository.findById(id);
    }

    @Override
    public boolean isSubcategoryExistsById(long id) {
        return subcategoryRepository.existsById(id);
    }

    @Override
    public boolean isSubcategoryDeletable(long id) {
        return isSubcategoryExistsById(id) && isSubcategoryHasNotProducts(id);
    }

    private boolean isSubcategoryHasNotProducts(long id) {
        return subcategoryRepository.findById(id)
                .map(subcategory -> subcategory.getProducts().isEmpty())
                .orElse(false);
    }
}
