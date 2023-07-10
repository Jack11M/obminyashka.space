package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.exception.not_found.EntityIdNotFoundException;
import space.obminyashka.items_exchange.service.impl.AdvertisementServiceImpl;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.NONEXISTENT_ENTITY_ID;

@ExtendWith(MockitoExtension.class)
class AdvertisementServiceTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private AdvertisementRepository advertisementRepository;
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    @InjectMocks
    private AdvertisementServiceImpl advertisementService;

    @BeforeEach
    void init() {
        messageSourceUtil.setMSource(messageSource);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isAdvertisementsHaveSubcategoryWithId_whenTryToCheckSubcategory_shouldSameResults(boolean expectedResult) {
        when(advertisementRepository.existsBySubcategoryId(anyLong())).thenReturn(expectedResult);

        final boolean result = advertisementService.areAdvertisementsExistWithSubcategory(NONEXISTENT_ENTITY_ID);
        assertEquals(expectedResult, result);
        verify(advertisementRepository).existsBySubcategoryId(NONEXISTENT_ENTITY_ID);
    }

    @Test
    void addFavorite_WhenDataCorrect_Successfully() {
        final var existedAdvId = UUID.randomUUID();
        final var existedUsername = "user";
        advertisementService.addFavorite(existedAdvId, existedUsername);

        verify(advertisementRepository).addFavoriteAdvertisementsByUsername(existedUsername, existedAdvId);
    }

    @Test
    void deleteFavorite_whenAdvertisementIsDeleted_shouldSuccessfullyEnd() {
        final var exceptedNumberOfDeletedAdv = 1;
        final var existedAdvId = UUID.randomUUID();
        final var existedUsername = "user";
        when(advertisementRepository.removeFavoriteAdvertisementsByIdAndUserUsername(any(), anyString()))
                .thenReturn(exceptedNumberOfDeletedAdv);

        advertisementService.deleteFavorite(existedAdvId, existedUsername);

        verify(advertisementRepository).removeFavoriteAdvertisementsByIdAndUserUsername(existedAdvId, existedUsername);
    }

    @Test
    void deleteFavorite_whenAdvertisementIsNotDeleted_shouldThrownException() {
        final var exceptedNumberOfDeletedAdv = 0;
        final var existedAdvId = UUID.randomUUID();
        final var existedUsername = "user";
        when(advertisementRepository.removeFavoriteAdvertisementsByIdAndUserUsername(any(), anyString()))
                .thenReturn(exceptedNumberOfDeletedAdv);

        assertAll(
                () -> assertThrows(EntityIdNotFoundException.class, () -> advertisementService.deleteFavorite(existedAdvId, existedUsername)),
                () -> verify(advertisementRepository).removeFavoriteAdvertisementsByIdAndUserUsername(existedAdvId, existedUsername)
        );
    }
}