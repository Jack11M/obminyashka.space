package space.obminyashka.items_exchange.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dao.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
@DBRider
class FavoriteAdvertisementDBTest {
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("advertisement/addFavoriteAdv.yml")
    void addFavoriteAdvertisement_whenDataCorrect_successfully() {
        UUID advertisementId = UUID.fromString("65e3ee49-5927-40be-aafd-0461ce45f295");
        final var advertisement = advertisementRepository.findById(advertisementId).orElse(null);
        final var user = userRepository.findByUsername("user").orElse(null);

        user.setFavoriteAdvertisements(List.of(advertisement));
        userRepository.save(user);

        assertAll(
                () -> assertTrue(user.getFavoriteAdvertisements().contains(advertisement)),
                () -> assertEquals(1, user.getFavoriteAdvertisements().size())
        );
    }
}