package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.CategoryControllerDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<String> findAllCategoryNames();

    List<CategoryControllerDto> findAllCategories();

    Optional<CategoryControllerDto> findCategoryById(Long id);

    List<String> findSubcategoryNamesByCategoryId(Long categoryId);

    Optional<CategoryControllerDto> addNewCategory(CategoryControllerDto categoryControllerDto);

    Optional<CategoryControllerDto> updateCategory(CategoryControllerDto categoryControllerDto);

    boolean removeCategoryById(Long categoryId);

    boolean removeSubcategoryById(Long subcategoryId);
}
