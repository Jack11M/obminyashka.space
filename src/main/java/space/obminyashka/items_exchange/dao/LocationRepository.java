package space.obminyashka.items_exchange.dao;

import space.obminyashka.items_exchange.model.Location;
import space.obminyashka.items_exchange.model.enums.I18n;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByIdIn(List<Long> ids);

    List<Location> findByI18n(I18n i18n);
}
