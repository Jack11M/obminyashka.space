package space.obminyashka.items_exchange.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("getResultsOfInteraction")
    void isAdvertisementsHaveSubcategoryWithId_whenTryToCheckSubcategory_shouldSameResults(boolean returnedValue,
                                                                                           boolean expectedResult) {
        when(advertisementRepository.existsBySubcategoryId(anyLong())).thenReturn(returnedValue);

        final boolean result = advertisementService.areAdvertisementsExistWithSubcategory(NONEXISTENT_ENTITY_ID);
        assertEquals(expectedResult, result);
        verify(advertisementRepository).existsBySubcategoryId(NONEXISTENT_ENTITY_ID);
    }

    private static Stream<Arguments> getResultsOfInteraction() {
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(false, false)
        );
    }
}