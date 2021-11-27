package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.model.Location;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByIdIn(List<Long> ids);

    List<Location> findByI18nIgnoreCase(String i18n);
}
