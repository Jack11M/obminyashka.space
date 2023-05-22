package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dao.SubcategoryRepository;
import space.obminyashka.items_exchange.model.Subcategory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.*;

@SpringBootTest
class AdvertisementServiceTest {

    @MockBean
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private AdvertisementService advertisementService;

    @Test
    void isAdvertisementsHaveSubcategoryWithId_whenSubcategoryDoesNotExistById_shouldReturnFalse() {
        when(advertisementRepository.existsBySubcategoryId(anyLong())).thenReturn(false);

        final boolean result = advertisementService.isAdvertisementsHaveSubcategoryWithId(NONEXISTENT_ENTITY_ID);
        assertFalse(result);
        verify(advertisementRepository, times(1)).existsBySubcategoryId(NONEXISTENT_ENTITY_ID);
    }
}