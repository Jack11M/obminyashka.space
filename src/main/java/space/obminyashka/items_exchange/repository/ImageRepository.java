package space.obminyashka.items_exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Image;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findByAdvertisementId(UUID id);

    void deleteAllByIdIn(List<UUID> id);

    boolean existsAllByIdInAndAdvertisement_Id(List<UUID> id, UUID advertisementId);

    int countImageByAdvertisement_Id(UUID id);

    @Query("select i.id from Image i where i.advertisement.id =:advertisementId")
    List<UUID> getImagesIdByAdvertisementId(UUID advertisementId);
}