package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.dto.SubcategoriesNamesDto;
import com.hillel.items_exchange.dto.SubcategoryVo;
import com.hillel.items_exchange.model.Subcategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;

    public void removeSubcategoryById(Long subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }

    public SubcategoriesNamesDto findSubcategoryNamesByCategoryId(Long categoryId) {
        SubcategoriesNamesDto subcategoriesNamesDto = new SubcategoriesNamesDto();
        subcategoriesNamesDto.setSubcategoriesNamesDto(Optional.ofNullable(subcategoryRepository.findSubcategoriesNamesByCategory(categoryId))
                .orElse(Collections.emptyList()));
        return subcategoriesNamesDto;
    }

    public boolean isSubcategoriesVoIdsInvalid(List<SubcategoryVo> subcategoryVos) {
        return subcategoryVos.stream()
                .filter(subcategoryDto -> subcategoryDto.getId() != 0)
                .map(SubcategoryVo::getId)
                .noneMatch(this::isSubcategoryVoIdValid);
    }

    public boolean isSubcategoryVoIdValid(Long subcategoryVoId) {
        return subcategoryRepository.existsById(subcategoryVoId);
    }

    public boolean isSubcategoryHasNotProducts(Long subcategoryVoId) {
        Optional<Subcategory> subcategory = subcategoryRepository.findById(subcategoryVoId);
        return subcategory.map(s -> s.getProducts().isEmpty()).orElse(false);
    }
}
