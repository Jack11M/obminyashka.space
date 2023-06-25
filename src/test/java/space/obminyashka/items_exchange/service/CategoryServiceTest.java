package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.CategoryRepository;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.mapper.SubcategoryMapper;
import space.obminyashka.items_exchange.model.Category;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.*;

@SpringBootTest
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private SubcategoryService subcategoryService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubcategoryMapper subcategoryMapper;
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
                       subcategoryMapper.toModelList(newCategoryDto.getSubcategories())));
        verify(categoryRepository, times(1)).saveAndFlush(newCategory);
    }

    @Test
    void removeById_whenExists_shouldRemoveCategory() {
        categoryService.removeById(EXISTING_ENTITY_ID);
        verify(categoryRepository).deleteById(EXISTING_ENTITY_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @ParameterizedTest
    @MethodSource("getTestCategoriesData")
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndNameAndSubcategories_shouldReturnTrue(CategoryDto testCategory, boolean expectedResult) {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(true);
        when(subcategoryService.findAllSubcategoryIds()).thenReturn(List.of(EXISTING_ENTITY_ID));

        final boolean result = categoryService.isCategoryDtoUpdatable(testCategory);
        assertEquals(expectedResult, result);
        verify(categoryRepository).existsByIdAndNameIgnoreCase(EXISTING_ENTITY_ID, EXISTING_CATEGORY_NAME);
        verify(subcategoryService).findAllSubcategoryIds();
    }

    private static Stream<Arguments> getTestCategoriesData() {
        return Stream.of(
                Arguments.of(getUpdatedCategoryDto(EXISTING_ENTITY_ID, EXISTING_ENTITY_ID, EXISTING_CATEGORY_NAME), true),
                Arguments.of(getUpdatedCategoryDto(NONEXISTENT_ENTITY_ID, EXISTING_ENTITY_ID, EXISTING_CATEGORY_NAME), false)
        );
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndCategoryNameHasNotDuplicateAndSubcategoriesExist_shouldReturnTrue() {
        prepareCategoryBasicMocks(false);
        when(subcategoryService.findAllSubcategoryIds()).thenReturn(List.of(EXISTING_ENTITY_ID));

        isCategoryDtoUpdatableBasicTest(true);
        verify(subcategoryService, times(1)).findAllSubcategoryIds();
    }

    @Test
    void isCategoryDtoUpdatable_whenCategoryExistsByIdAndCategoryNameHasDuplicate_shouldReturnFalse() {
        prepareCategoryBasicMocks(true);

        isCategoryDtoUpdatableBasicTest(false);
    }

    private void prepareCategoryBasicMocks(boolean isExists) {
        when(categoryRepository.existsByIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.existsByNameIgnoreCase(anyString())).thenReturn(isExists);
    }

    private void isCategoryDtoUpdatableBasicTest(boolean expectedResult) {
        final CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID,
                EXISTING_ENTITY_ID,
                CATEGORY_NAME_TOYS);

        final boolean result = categoryService.isCategoryDtoUpdatable(updatedCategoryDto);
        assertEquals(expectedResult, result);
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

    @ParameterizedTest
    @MethodSource("getTestCategories")
    void isCategoryDtoValidForCreating_whenCategoryNameHasNotDuplicateAndAllItsSubcategoryIdsEqualsZero_shouldReturnTrue(
            boolean isExists, CategoryDto dto, boolean expectedResult, String categoryName) {
        when(categoryRepository.existsByNameIgnoreCase(anyString())).thenReturn(isExists);

        final boolean result = categoryService.isCategoryDtoValidForCreating(dto);
        assertEquals(expectedResult, result);
        verify(categoryRepository).existsByNameIgnoreCase(categoryName);
    }

    private static Stream<Arguments> getTestCategories() {
        return Stream.of(
                Arguments.of(false, createNonExistValidCategoryDto(), true, CATEGORY_NAME_TOYS),
                Arguments.of(true, createCategoryDtoWithDuplicateName(), false, EXISTING_CATEGORY_NAME)
        );
    }
}