package space.obminyashka.items_exchange.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dao.SubcategoryRepository;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Subcategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class SubcategoryServiceTest {

    @MockBean
    private SubcategoryRepository subcategoryRepository;
    @MockBean
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private SubcategoryService subcategoryService;
    private Subcategory existingSubcategory;

    @BeforeEach
    void setUp() {
        existingSubcategory = createSubcategory(EXISTING_ENTITY_ID, EXISTING_SUBCATEGORY_NAME);
    }

    @Test
    void findSubcategoryNamesByCategoryId_whenCategoryExists_shouldReturnAllItsSubcategoryNames() {
        final List<String> allSubcategoryNames = List.of(SUBCATEGORY_NAME_MEN_SHOES,
                SUBCATEGORY_NAME_WINTER_SHOES);
        when(subcategoryRepository.findSubcategoriesNamesByCategory(anyLong()))
                .thenReturn(allSubcategoryNames);

        List<String> allSubcategoryNamesOfExistingCategory =
                subcategoryService.findSubcategoryNamesByCategoryId(EXISTING_ENTITY_ID);

        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(2, allSubcategoryNamesOfExistingCategory.size()),
                () -> assertEquals(SUBCATEGORY_NAME_MEN_SHOES, allSubcategoryNamesOfExistingCategory.get(0)),
                () -> assertEquals(SUBCATEGORY_NAME_WINTER_SHOES, allSubcategoryNamesOfExistingCategory.get(1)));
        verify(subcategoryRepository, times(1))
                .findSubcategoriesNamesByCategory(EXISTING_ENTITY_ID);
    }

    @Test
    void findSubcategoryNamesByCategoryId_whenCategoryDoesNotExist_shouldReturnEmptyList() {
        when(subcategoryRepository.findSubcategoriesNamesByCategory(anyLong())).thenReturn(Collections.emptyList());

        List<String> allSubcategories = subcategoryService.findSubcategoryNamesByCategoryId(NONEXISTENT_ENTITY_ID);
        assertEquals(Collections.emptyList(), allSubcategories);
        verify(subcategoryRepository, times(1))
                .findSubcategoriesNamesByCategory(NONEXISTENT_ENTITY_ID);
    }

    @Test
    void removeSubcategoryById_whenExists_shouldRemoveSubcategory() {
        subcategoryService.removeSubcategoryById(EXISTING_ENTITY_ID);
        verify(subcategoryRepository).deleteById(EXISTING_ENTITY_ID);
        verifyNoMoreInteractions(subcategoryRepository);
    }

    @Test
    void findById_whenExists_shouldReturnSubcategory() {
        when(subcategoryRepository.findById(anyLong())).thenReturn(Optional.of(existingSubcategory));

        final Subcategory subcategory = subcategoryService.findById(EXISTING_ENTITY_ID)
                .orElse(new Subcategory());
        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(EXISTING_ENTITY_ID, subcategory.getId()),
                () -> assertEquals(EXISTING_SUBCATEGORY_NAME, subcategory.getName()));
        verify(subcategoryRepository, times(1)).findById(EXISTING_ENTITY_ID);
    }

    @Test
    void findById_whenDoesNotExist_shouldReturnOptionalEmpty() {
        when(subcategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        final Optional<Subcategory> subcategory = subcategoryService.findById(NONEXISTENT_ENTITY_ID);
        assertEquals(Optional.empty(), subcategory);
        verify(subcategoryRepository, times(1)).findById(NONEXISTENT_ENTITY_ID);
    }

    @Test
    void isSubcategoryExistsById_whenSubcategoryExists_shouldReturnTrue() {
        when(subcategoryRepository.existsById(anyLong())).thenReturn(true);

        final boolean result = subcategoryService.isSubcategoryExistsById(EXISTING_ENTITY_ID);
        assertTrue(result);
        verify(subcategoryRepository, times(1)).existsById(EXISTING_ENTITY_ID);
    }

    @Test
    void isSubcategoryExistsById_whenSubcategoryDoesNotExist_shouldReturnFalse() {
        when(subcategoryRepository.existsById(anyLong())).thenReturn(false);

        final boolean result = subcategoryService.isSubcategoryExistsById(NONEXISTENT_ENTITY_ID);
        assertFalse(result);
        verify(subcategoryRepository, times(1)).existsById(NONEXISTENT_ENTITY_ID);
    }

    @Test
    void isSubcategoryDeletable_whenSubcategoryDoesNotExistById_shouldReturnFalse() {
        when(advertisementRepository.existsBySubcategoryId(anyLong())).thenReturn(false);

        final boolean result = subcategoryService.isSubcategoryDeletable(NONEXISTENT_ENTITY_ID);
        assertFalse(result);
        verify(advertisementRepository, times(1)).existsBySubcategoryId(NONEXISTENT_ENTITY_ID);
    }

    @Test
    void findAllSubcategoryIds_whenSubcategoriesExist_shouldReturnListSubcategoryIds() {
        final List<Subcategory> subcategories = List.of(existingSubcategory, createSubcategory(2L,
                SUBCATEGORY_NAME_WINTER_SHOES));
        when(subcategoryRepository.findAll()).thenReturn(subcategories);

        List<Long> allSubcategoriesIds = subcategoryService.findAllSubcategoryIds();
        assertAll(DATA_EQUALS_CHECKING,
                () -> assertEquals(2, allSubcategoriesIds.size()),
                () -> assertEquals(EXISTING_ENTITY_ID, (long) allSubcategoriesIds.get(0)),
                () -> assertEquals(CATEGORY_TOYS_ID, (long) allSubcategoriesIds.get(1)));
        verify(subcategoryRepository, times(1)).findAll();
    }

    @Test
    void findAllSubcategoryIds_whenSubcategoriesDoesNotExist_shouldReturnEmptyList() {
        when(subcategoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Long> allSubcategoriesIds = subcategoryService.findAllSubcategoryIds();
        assertEquals(Collections.emptyList(), allSubcategoriesIds);
        verify(subcategoryRepository, times(1)).findAll();
    }
}