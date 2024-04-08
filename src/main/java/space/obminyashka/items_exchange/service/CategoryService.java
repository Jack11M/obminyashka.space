package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.repository.enums.Size;
import space.obminyashka.items_exchange.rest.dto.CategoryDto;

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
    List<CategoryDto> findAllCategoryDtos();

    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId is Category ID
     * @return category entity from DB that is represented as {@link CategoryDto}
     */
    Optional<CategoryDto> findCategoryDtoById(long categoryId);

    /**
     * Creates a new category in the table 'category' and subcategory child records in table 'subcategory'
     * or updates the existing category record and subcategory child records in the database.
     *
     * @param categoryDto must not be null.
     * @return the created or updated Category DTO; will never be null.
     */
    CategoryDto saveCategoryWithSubcategories(CategoryDto categoryDto);

    /**
     * Removes the category with the given ID and subcategory child records from DB.
     *
     * @param categoryId is Category ID to remove.
     */
    void removeById(long categoryId);

    /**
     * If a category exists, returns {@code true}, otherwise {@code false}.
     *
     * @param id is Category ID.
     * @return true if a Category with the given id exists, false otherwise.
     */
    boolean isCategoryExistsById(long id);

    /**
     * Checks if a category with the given ID exists in DB and internal subcategories have no advertisements.
     *
     * @param categoryId is Category ID.
     * @return {@code true} if a category with the given ID can be deleted, {@code false} otherwise.
     */
    boolean isCategoryDeletable(long categoryId);

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

    /**
     * Return sizes for 'Clothing' and 'Shoes' categories
     * @param id category identifier
     * @return sized of selected category
     * @see Size.Clothing
     * @see Size.Shoes
     */
    List<String> findSizesForCategory(long id);
}
