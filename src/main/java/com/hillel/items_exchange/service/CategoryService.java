package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.CategoryDto;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    /**
     * Returns all category names.
     *
     * @return list of category names from DB that are represented as {@link String}
     */
    List<String> findAllCategoryNames();

    /**
     * Returns all Category DTOs.
     *
     * @return list of category entities from DB that are represented as {@link CategoryDto}
     */
    List<CategoryDto> findAllCategoryDto();

    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId is Category ID
     * @return category entity from DB that is represented as {@link CategoryDto}
     */
    Optional<CategoryDto> findCategoryDtoById(long categoryId);

    /**
     * Creates a new record in table category in DB.
     *
     * @param categoryDto must not be null.
     * @return the created Category DTO; will never be null.
     */
    CategoryDto create(CategoryDto categoryDto);

    /**
     * Updates the existing record in table category in DB.
     *
     * @param categoryDto must not be null.
     * @return the updated Category DTO; will never be null.
     */
    CategoryDto update(CategoryDto categoryDto);

    /**
     * Removes the category with the given ID from DB.
     *
     * @param categoryId is Category ID to remove.
     */
    void removeById(long categoryId);

    /**
     * Checks if a category with the given ID exists in DB and internal subcategories have not products.
     *
     * @param categoryId is Category ID.
     * @return {@code true} if a category with the given ID can be deleted, {@code false} otherwise.
     */
    boolean isCategoryDtoDeletable(long categoryId);

    /**
     * Checks if the category DTO ID exists in DB, updated category name has not duplicates and internal subcategories exist or have IDs equals zero.
     *
     * @param categoryDto must not be null.
     * @return {@code true} if a category can be updated, {@code false} otherwise.
     */
    boolean isCategoryDtoUpdatable(CategoryDto categoryDto);

    /**
     * Checks if the category DTO ID and internal subcategory IDs are zero and the category name has no duplicates.
     *
     * @param categoryDto must not be null.
     * @return {@code true} if a category can be created, {@code false} otherwise.
     */
    boolean isCategoryDtoValidForCreating(CategoryDto categoryDto);
}
