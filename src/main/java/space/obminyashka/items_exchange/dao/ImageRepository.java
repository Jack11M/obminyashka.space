package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.model.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByAdvertisementId(Long id);

    void deleteAllByIdIn(List<Long> id);

    boolean existsAllByIdInAndAdvertisement_Id(List<Long> id, Long advertisementId);

    int countImageByAdvertisement_Id(long id);
}