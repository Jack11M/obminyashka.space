package space.obminyashka.items_exchange.util.data_producer;

import org.apache.commons.lang3.RandomStringUtils;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.rest.dto.CategoryDto;
import space.obminyashka.items_exchange.rest.response.SubcategoryView;
import space.obminyashka.items_exchange.repository.model.Category;
import space.obminyashka.items_exchange.repository.model.Subcategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class CategoriesDataProducer {

    public static final long EXISTING_ENTITY_ID = 2L;
    public static final long NEW_ENTITY_ID = 0L;
    public static final long NONEXISTENT_ENTITY_ID = 111111L;
    public static final long CATEGORY_CLOTHING_ID = 1L;
    public static final long CATEGORY_TOYS_ID = 3L;
    public static final String NEW_CATEGORY_NAME = "new category";
    public static final String EXISTING_CATEGORY_NAME = "Shoes";
    public static final String EXISTING_SUBCATEGORY_NAME = "light_shoes";
    public static final String NEW_SUBCATEGORY_NAME = "new subcategory";
    public static final String OTHER_NEW_SUBCATEGORY_NAME = "other new subcategory";
    public static final String INVALID_CATEGORY_NAME = "xx";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String USERNAME_ADMIN = "admin";
    public static final String CATEGORY_NAME_TOYS = "Children`S Furniture";
    public static final String SUBCATEGORY_NAME_WINTER_SHOES = "winter shoes";
    public static final String SUBCATEGORY_NAME_MEN_SHOES = "men shoes";
    public static final String SUBCATEGORY_NAME_DOLLS = "dolls";
    public static final String DATA_EQUALS_CHECKING = "Checking objects' data equal";
    public static final String SUBCATEGORY_NAME_FAIRY_TALES = "fairy tales";
    public static final String SUBCATEGORY_NAME_LEGO = "lego";

    protected static CategoryDto createExistCategoryDto() {
        final SubcategoryView lightShoes = new SubcategoryView(EXISTING_ENTITY_ID, EXISTING_SUBCATEGORY_NAME);
        return new CategoryDto(EXISTING_ENTITY_ID, EXISTING_CATEGORY_NAME, Collections.singletonList(lightShoes));
    }

    public static CategoryDto createNonExistValidCategoryDto() {
        final SubcategoryView fairyTales = new SubcategoryView(NEW_ENTITY_ID, SUBCATEGORY_NAME_FAIRY_TALES);
        final SubcategoryView educationalBooks = new SubcategoryView(NEW_ENTITY_ID, SUBCATEGORY_NAME_LEGO);
        return new CategoryDto(NEW_ENTITY_ID, CATEGORY_NAME_TOYS, Arrays.asList(fairyTales, educationalBooks));
    }

    public static CategoryDto createNonExistCategoryDtoWithInvalidName() {
        final SubcategoryView fairyTales = new SubcategoryView(NEW_ENTITY_ID, SUBCATEGORY_NAME_FAIRY_TALES);
        final SubcategoryView educationalBooks = new SubcategoryView(NEW_ENTITY_ID, SUBCATEGORY_NAME_LEGO);
        return new CategoryDto(NEW_ENTITY_ID, INVALID_CATEGORY_NAME, Arrays.asList(fairyTales, educationalBooks));
    }

    public static CategoryDto createNonExistCategoryDtoWithInvalidSubcategory() {
        final var categoryDto = createNewInvalidCategoryDto(EXISTING_ENTITY_ID, NEW_CATEGORY_NAME);
        categoryDto.getSubcategories().add(new SubcategoryView(0, RandomStringUtils.randomAlphabetic(60)));
        return categoryDto;
    }

    public static CategoryDto createCategoryDtoWithDuplicateName() {
        return createNewInvalidCategoryDto(NEW_ENTITY_ID, EXISTING_CATEGORY_NAME);
    }

    public static CategoryDto getUpdatedCategoryDto(long subcategoryId, long categoryId, String updatedCategoryName) {
        final CategoryDto updatedCategoryDto = createExistCategoryDto();
        updatedCategoryDto.setSubcategories(Arrays.asList(new SubcategoryView(subcategoryId, SUBCATEGORY_NAME_MEN_SHOES),
                new SubcategoryView(NEW_ENTITY_ID, SUBCATEGORY_NAME_WINTER_SHOES)));

        updatedCategoryDto.setId(categoryId);
        updatedCategoryDto.setName(updatedCategoryName);
        return updatedCategoryDto;
    }

    public static CategoryDto getUpdatedCategoryDtoWithInvalidName() {
        final CategoryDto updatedCategoryDto = createExistCategoryDto();
        updatedCategoryDto.setName(INVALID_CATEGORY_NAME);
        return updatedCategoryDto;
    }
    private static CategoryDto createNewInvalidCategoryDto(long subcategoryId,
                                                           String categoryName) {

        final SubcategoryView newValidSubcategory = new SubcategoryView(NEW_ENTITY_ID, NEW_SUBCATEGORY_NAME);
        final SubcategoryView otherNewSubcategory = new SubcategoryView(subcategoryId, OTHER_NEW_SUBCATEGORY_NAME);
        return new CategoryDto(CategoriesDataProducer.NEW_ENTITY_ID, categoryName, new ArrayList<>(Arrays.asList(newValidSubcategory, otherNewSubcategory)));
    }

    public static List<String> createCategoryNamesList() {
        return List.of(EXISTING_CATEGORY_NAME, CATEGORY_NAME_TOYS);
    }

    public static Category createCategoryShoes() {
        return createExistingCategory(EXISTING_ENTITY_ID, EXISTING_CATEGORY_NAME, EXISTING_ENTITY_ID,
                EXISTING_SUBCATEGORY_NAME);
    }

    public static Category createCategoryToys() {
        return createExistingCategory(CATEGORY_TOYS_ID, CATEGORY_NAME_TOYS, CATEGORY_TOYS_ID,
                SUBCATEGORY_NAME_DOLLS);
    }

    public static Category createNewCategory() {
        final Subcategory fairyTales = createSubcategory(EXISTING_ENTITY_ID, SUBCATEGORY_NAME_FAIRY_TALES);
        final Subcategory educationalBooks = createSubcategory(CATEGORY_TOYS_ID, SUBCATEGORY_NAME_LEGO);
        return new Category(EXISTING_ENTITY_ID, CATEGORY_NAME_TOYS, List.of(fairyTales, educationalBooks));
    }

    public static Subcategory createSubcategory(long subcategoryId, String subcategoryName) {
        final Subcategory newSubcategory = new Subcategory();
        newSubcategory.setId(subcategoryId);
        newSubcategory.setName(subcategoryName);
        newSubcategory.setAdvertisements(Collections.emptyList());
        return newSubcategory;
    }

    public static List<Category> createAllCategoriesList() {
        return List.of(createCategoryShoes(), createCategoryToys());
    }

    private static Category createExistingCategory(long categoryId,
                                                   String categoryName,
                                                   long subcategoryId,
                                                   String subcategoryName) {

        final Subcategory subcategory = createSubcategory(subcategoryId, subcategoryName);
        return new Category(categoryId, categoryName, Collections.singletonList(subcategory));
    }
}
