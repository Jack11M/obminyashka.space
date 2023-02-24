package space.obminyashka.items_exchange.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;

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

        final var firstTitlesGetAttempt = advertisementService.findRandom12Thumbnails();

        assertAll("ValidationMessage of all parameters and mocks",
                () -> assertFalse(firstTitlesGetAttempt.isEmpty()),
                () -> assertEquals(repository.count(), firstTitlesGetAttempt.size())
        );

        final var secondTitlesGetAttempt = advertisementService.findRandom12Thumbnails();
        assertEquals(firstTitlesGetAttempt, secondTitlesGetAttempt, "Collections must be equals because of caching response");
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void findPaginated_shouldReturnEmpty() {
        long id = 1;
        int page = 0;
        int size = 12;
        final var firstTitlesGetAttempt = advertisementService.findByCategoryId(id, PageRequest.of(page, size));

        assertAll("ValidationMessage of all parameters and mocks",
                () -> assertFalse(firstTitlesGetAttempt.isEmpty()),
                () -> assertEquals(repository.count(), firstTitlesGetAttempt.getTotalElements())
        );

        final var secondTitlesGetAttempt = advertisementService.findByCategoryId(1L, PageRequest.of(0, 12));
        assertEquals(firstTitlesGetAttempt, secondTitlesGetAttempt, "Paginated must be equals because of caching response");
    }
}