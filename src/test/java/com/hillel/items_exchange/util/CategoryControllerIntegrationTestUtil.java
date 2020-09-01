package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.dto.SubcategoryDto;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;

@NoArgsConstructor
public class CategoryControllerIntegrationTestUtil {

    public static final long EXISTING_ENTITY_ID = 1L;
    public static final long NEW_ENTITY_ID = 0L;
    public static final long NONEXISTENT_ENTITY_ID = 111111L;
    public static final String NEW_CATEGORY_NAME = "new category";
    public static final String EXISTING_CATEGORY_NAME = "shoes";
    public static final String EXISTING_SUBCATEGORY_NAME = "light_shoes";
    public static final String NEW_SUBCATEGORY_NAME = "new subcategory";
    public static final String OTHER_NEW_SUBCATEGORY_NAME = "other new subcategory";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String USERNAME_ADMIN = "admin";

    protected static CategoryDto createExistCategoryDto() {
        SubcategoryDto lightShoes = new SubcategoryDto(EXISTING_ENTITY_ID, EXISTING_SUBCATEGORY_NAME);
        return new CategoryDto(EXISTING_ENTITY_ID, EXISTING_CATEGORY_NAME, Collections.singletonList(lightShoes));
    }

    protected static CategoryDto createNonExistValidCategoryDto() {
        SubcategoryDto fairyTales = new SubcategoryDto(NEW_ENTITY_ID, "fairy tales");
        SubcategoryDto educationalBooks = new SubcategoryDto(NEW_ENTITY_ID, "educational books");
        return new CategoryDto(NEW_ENTITY_ID, "books", Arrays.asList(fairyTales, educationalBooks));
    }

    public static CategoryDto createNonExistCategoryDtoWithInvalidId() {
        return createNewInvalidCategoryDto(EXISTING_ENTITY_ID, NEW_ENTITY_ID, NEW_CATEGORY_NAME);
    }

    protected static CategoryDto createNonExistCategoryDtoWithInvalidSubcategoryId() {
        return createNewInvalidCategoryDto(NEW_ENTITY_ID, EXISTING_ENTITY_ID, NEW_CATEGORY_NAME);
    }

    protected static CategoryDto createCategoryDtoWithDuplicateName() {
        return createNewInvalidCategoryDto(NEW_ENTITY_ID, NEW_ENTITY_ID, EXISTING_CATEGORY_NAME);
    }

    protected static CategoryDto getUpdatedCategoryDto(long subcategoryId, long categoryId, String updatedCategoryName) {
        CategoryDto updatedCategoryDto = createExistCategoryDto();
        updatedCategoryDto.setSubcategories(Arrays.asList(new SubcategoryDto(subcategoryId, "men shoes"),
                new SubcategoryDto(NEW_ENTITY_ID, "winter shoes")));

        updatedCategoryDto.setId(categoryId);
        updatedCategoryDto.setName(updatedCategoryName);
        return updatedCategoryDto;
    }

    private static CategoryDto createNewInvalidCategoryDto(long categoryId,
                                                    long subcategoryId,
                                                    String categoryName) {

        SubcategoryDto newValidSubcategory = new SubcategoryDto(NEW_ENTITY_ID, NEW_SUBCATEGORY_NAME);
        SubcategoryDto otherNewSubcategory = new SubcategoryDto(subcategoryId, OTHER_NEW_SUBCATEGORY_NAME);
        return new CategoryDto(categoryId, categoryName, Arrays.asList(newValidSubcategory, otherNewSubcategory));
    }

}
