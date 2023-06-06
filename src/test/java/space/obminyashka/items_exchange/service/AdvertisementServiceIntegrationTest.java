package space.obminyashka.items_exchange.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.controller.request.AdvertisementFindRequest;

import jakarta.transaction.Transactional;
import space.obminyashka.items_exchange.dto.AdvertisementFilterDto;
import space.obminyashka.items_exchange.dto.AdvertisementTitleDto;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@DBRider
class AdvertisementServiceIntegrationTest {
    @Autowired
    private AdvertisementRepository repository;
    @Autowired
    private AdvertisementService advertisementService;

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void findRandom12Thumbnails_shouldReturnEmpty() {
        AdvertisementFindRequest findAdvsRequest = new AdvertisementFindRequest();
        findAdvsRequest.setSize(12);
        final var firstTitlesGetAttempt = advertisementService.findRandomNThumbnails(findAdvsRequest);

        assertAll("ValidationMessage of all parameters and mocks",
                () -> assertFalse(firstTitlesGetAttempt.isEmpty()),
                () -> assertEquals(repository.count(), firstTitlesGetAttempt.size())
        );

        final var secondTitlesGetAttempt = advertisementService.findRandomNThumbnails(findAdvsRequest);
        assertEquals(firstTitlesGetAttempt, secondTitlesGetAttempt, "Collections must be equals because of caching response");
    }

//    @ParameterizedTest
//    @MethodSource("advertisementFilterDtoProvider")
//    @DataSet("database_init.yml")
//    void findFirst10ByFilter_ShouldReturnMatchingAdvertisements(AdvertisementFilterDto dto, int size) {
//        <AdvertisementTitleDto> result = advertisementService.findAdvertisementByFilter(dto);
//
//        assertEquals(size, result.size());
//    }
//
//    private static Set<Arguments> advertisementFilterDtoProvider() {
//        return Set.of(
//                arguments(new AdvertisementFilterDto(Set.of(AgeRange.OLDER_THAN_14), Set.of(Gender.MALE), Set.of(Season.SUMMER),
//                        Set.of("40"), new HashSet<>(), new HashSet<>(), null), 1),
//                arguments(new AdvertisementFilterDto(Set.of(AgeRange.FROM_3_TO_5, AgeRange.FROM_6_TO_9), Set.of(Gender.MALE, Gender.FEMALE),
//                        Set.of(Season.SUMMER, Season.DEMI_SEASON), Set.of("28", "40"), Set.of(1L), Set.of(),
//                        UUID.fromString("2c5467f3-b7ee-48b1-9451-7028255b757b")), 2));
//    }
}