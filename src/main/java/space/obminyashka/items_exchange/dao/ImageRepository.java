package space.obminyashka.items_exchange.dao;

import space.obminyashka.items_exchange.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByAdvertisementId(Long id);

}