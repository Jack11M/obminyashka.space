package space.obminyashka.items_exchange.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Image;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findByAdvertisementId(UUID id);

    void deleteAllByIdIn(List<UUID> id);

    boolean existsAllByIdInAndAdvertisementId(List<UUID> id, UUID advertisementId);

    int countImageByAdvertisementId(UUID id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into image(id, advertisement_id, resource) " +
            "values(:id, :advertisementId, :resource)")
    void createImage(UUID id, UUID advertisementId, byte[] resource);
}