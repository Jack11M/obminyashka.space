package space.obminyashka.items_exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Image;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findByAdvertisementId(UUID id);

    void deleteAllByIdIn(List<UUID> id);

    boolean existsAllByIdInAndAdvertisementId(List<UUID> id, UUID advertisementId);

    int countImageByAdvertisementId(UUID id);
}