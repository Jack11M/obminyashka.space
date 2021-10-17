package space.obminyashka.items_exchange.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DBRider
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
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

        assertAll("Validation of all parameters and mocks",
                () -> assertFalse(firstTitlesGetAttempt.isEmpty()),
                () -> assertEquals(repository.count(), firstTitlesGetAttempt.size())
        );

        final var secondTitlesGetAttempt = advertisementService.findRandom12Thumbnails();
        assertEquals(firstTitlesGetAttempt, secondTitlesGetAttempt, "Collections must be equals because of caching response");
    }
}