package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;
import space.obminyashka.items_exchange.rest.request.LocationRaw;

@Entity
@SuperBuilder
@NoArgsConstructor
public class Area extends BaseLocation {

    public Area(LocationRaw locationRaw) {
        super(locationRaw.getAreaUa(), locationRaw.getAreaEn());
    }

    @Override
    public String formatForSQL() {
        return "(UUID_TO_BIN('%s'),'%s','%s') ".formatted(id, nameEn, nameUa);
    }

}