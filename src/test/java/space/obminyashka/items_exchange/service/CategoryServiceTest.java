package space.obminyashka.items_exchange.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import space.obminyashka.items_exchange.dao.CategoryRepository;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.model.Category;
import space.obminyashka.items_exchange.model.Subcategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static space.obminyashka.items_exchange.mapper.UtilMapper.convertToDto;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private SubcategoryService subcategoryService;
    @Autowired
    private CategoryService categoryService;
    private Category categoryShoes;

    @BeforeEach
    void setUp() {
        categoryShoes = createCategoryShoes();
    }

    @Test
    void findAllCategoryNames_whenCategoriesExist_shouldReturnAllCategoryNames() {
        final List<String> categoryNames = createCategoryNamesList();
        when(categoryRepository.findAllCategoriesNames()).thenReturn(categoryNames);

        List<String> allCategoryNames = categoryService.findAllCategoryNames();
        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(2, allCategoryNames.size()),
                () -> assertEquals(categoryShoes.getName(), allCategoryNames.get(0)),
                () -> assertEquals(CATEGORY_NAME_TOYS, allCategoryNames.get(1)));
        verify(categoryRepository, times(1)).findAllCategoriesNames();
    }

    @Test
    void findAllCategoryNames_whenCategoriesDoNotExist_shouldReturnEmptyList() {
        when(categoryRepository.findAllCategoriesNames()).thenReturn(Collections.emptyList());

        List<String> allCategoryNames = categoryService.findAllCategoryNames();
        assertEquals(Collections.emptyList(), allCategoryNames);
        verify(categoryRepository, times(1)).findAllCategoriesNames();
    }

    @Test
    void findAllCategoryDto_whenCategoriesExist_shouldReturnAllCategoryDtos() {
        final List<Category> categories = createAllCategoriesList();
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDto> allCategories = categoryService.findAllCategoryDtos();
        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(2, allCategories.size()),
                () -> assertEquals(categoryShoes.getId(), allCategories.get(0).getId()),
                () -> assertEquals(CATEGORY_TOYS_ID, allCategories.get(1).getId()));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findAllCategoryDto_whenCategoriesDoNotExist_shouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDto> allCategories = categoryService.findAllCategoryDtos();
        assertEquals(Collections.emptyList(), allCategories);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findCategoryDtoById_whenExistsById_shouldReturnCategoryDto() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryShoes));

        final CategoryDto categoryDto = categoryService.findCategoryDtoById(EXISTING_ENTITY_ID)
                .orElse(new CategoryDto());
        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(categoryShoes.getId(), categoryDto.getId()),
                () -> assertEquals(categoryShoes.getName(), categoryDto.getName()));
        verify(categoryRepository, times(1)).findById(EXISTING_ENTITY_ID);
    }

    @Test
    void findCategoryDtoById_whenDoesNotExist_shouldReturnOptionalEmpty() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        final Optional<CategoryDto> categoryDto = categoryService.findCategoryDtoById(NONEXISTENT_ENTITY_ID);
        assertEquals(Optional.empty(), categoryDto);
        verify(categoryRepository, times(1)).findById(NONEXISTENT_ENTITY_ID);
    }

    @Test
    void saveCategoryWithSubcategories_shouldReturnNewCategory() {
        final CategoryDto categoryDto = createNonExistValidCategoryDto();
        final Category newCategory = createNewCategory();
        when(categoryRepository.saveAndFlush(any())).thenReturn(newCategory);

        final CategoryDto newCategoryDto = categoryService.saveCategoryWithSubcategories(categoryDto);
        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(newCategory.getId(), newCategoryDto.getId()),
                () -> assertEquals(newCategory.getName(), newCategoryDto.getName()),
                () -> assertEquals(2, newCategoryDto.getSubcategories().size()),
                () -> assertEquals(newCategory.getSubcategories(),
                        convertToDto(newCategoryDto.getSubcategories(), Subcategory.class)));
        verify(categoryRepository, times(1)).saveAndFlush(newCategory);
    }

    @Test
    void removeById_whenExists_shouldRemoveCategory() {
        categoryService.removeById(EXISTING_ENTITY_ID);
        verify(categoryRepository).deleteById(EXISTING_ENTITY_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void isCategoryDtoDeletable_whenCategoryExistsByIdAndInternalSubcategoriesHaveNotAdvertisements_shouldReturnTrue() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoryShoes));
        final boolean result = categoryService.isCategoryDtoDeletable(EXISTING_ENTITY_ID);
        verify(categoryRepository, times(1)).findById(EXISTING_ENTITY_ID);
        assertTrue(result);
    }

    @Test
    void isCategoryDtoDeletable_whenCategoryDoesNotExistsById_shouldReturnFalse() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        final boolean result = categoryService.isCategoryDtoDeletable(EXISTING_ENTITY_ID);
        verify(categoryRepository, times(1)).findById(EXISTING_ENTITY_ID);
        assertFalse(result);
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndNameAndSubcategoriesExist_shouldReturnTrue() {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(true);
        when(subcategoryService.findAllSubcategoryIds()).thenReturn(List.of(EXISTING_ENTITY_ID));

        final CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID,
                EXISTING_ENTITY_ID,
                EXISTING_CATEGORY_NAME);

        final boolean result = categoryService.isCategoryDtoUpdatable(updatedCategoryDto);
        assertTrue(result);
        verify(categoryRepository, times(1)).existsByIdAndNameIgnoreCase(EXISTING_ENTITY_ID,
                EXISTING_CATEGORY_NAME);
        verify(subcategoryService, times(1)).findAllSubcategoryIds();
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndCategoryNameHasNotDuplicateAndSubcategoriesExist_shouldReturnTrue() {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(subcategoryService.findAllSubcategoryIds()).thenReturn(List.of(EXISTING_ENTITY_ID));

        final CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID,
                EXISTING_ENTITY_ID,
                UPDATED_CATEGORY_NAME);

        final boolean result = categoryService.isCategoryDtoUpdatable(updatedCategoryDto);
        assertTrue(result);
        verify(categoryRepository, times(1)).existsByIdAndNameIgnoreCase(EXISTING_ENTITY_ID,
                UPDATED_CATEGORY_NAME);
        verify(categoryRepository, times(1)).existsById(EXISTING_ENTITY_ID);
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(UPDATED_CATEGORY_NAME);
        verify(subcategoryService, times(1)).findAllSubcategoryIds();
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndCategoryNameHasDuplicate_shouldReturnFalse() {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        final CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID,
                EXISTING_ENTITY_ID,
                CATEGORY_NAME_TOYS);

        final boolean result = categoryService.isCategoryDtoUpdatable(updatedCategoryDto);
        assertFalse(result);
        verify(categoryRepository, times(1)).existsByIdAndNameIgnoreCase(EXISTING_ENTITY_ID,
                CATEGORY_NAME_TOYS);
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(CATEGORY_NAME_TOYS);
        verify(categoryRepository, times(1)).existsById(EXISTING_ENTITY_ID);
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryDoesNotExistsById_shouldReturnFalse() {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);

        final CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID,
                NONEXISTENT_ENTITY_ID,
                EXISTING_CATEGORY_NAME);

        final boolean result = categoryService.isCategoryDtoUpdatable(updatedCategoryDto);
        assertFalse(result);
        verify(categoryRepository, times(1)).existsByIdAndNameIgnoreCase(NONEXISTENT_ENTITY_ID,
                EXISTING_CATEGORY_NAME);
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndNameAndSubcategoriesDoNotExist_shouldReturnFalse() {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(true);
        when(subcategoryService.findAllSubcategoryIds()).thenReturn(List.of(EXISTING_ENTITY_ID));

        final CategoryDto updatedCategoryDto = getUpdatedCategoryDto(NONEXISTENT_ENTITY_ID,
                EXISTING_ENTITY_ID,
                EXISTING_CATEGORY_NAME);

        final boolean result = categoryService.isCategoryDtoUpdatable(updatedCategoryDto);
        assertFalse(result);
        verify(categoryRepository, times(1)).existsByIdAndNameIgnoreCase(EXISTING_ENTITY_ID,
                EXISTING_CATEGORY_NAME);
        verify(subcategoryService, times(1)).findAllSubcategoryIds();
    }

    @Test
    void isCategoryDtoValidForCreating_whenCategoryNameHasNotDuplicateAndAllItsSubcategoryIdsEqualsZero_shouldReturnTrue() {
        when(categoryRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);

        final CategoryDto newValidCategoryDto = createNonExistValidCategoryDto();
        final boolean result = categoryService.isCategoryDtoValidForCreating(newValidCategoryDto);
        assertTrue(result);
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(CATEGORY_NAME_BOOKS);
    }

    @Test
    void isCategoryDtoValidForCreating_whenCategoryNameHasDuplicate_shouldReturnFalse() {
        when(categoryRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        final CategoryDto newInvalidCategoryDto = createCategoryDtoWithDuplicateName();
        final boolean result = categoryService.isCategoryDtoValidForCreating(newInvalidCategoryDto);
        assertFalse(result);
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(EXISTING_CATEGORY_NAME);
    }
}