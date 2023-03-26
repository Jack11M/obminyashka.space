package space.obminyashka.items_exchange.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.controller.request.AdvertisementFindRequest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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

}