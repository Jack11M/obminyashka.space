package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;
import space.obminyashka.items_exchange.rest.request.RawLocation;

@Entity
@NoArgsConstructor
public class Area extends BaseLocation {

    public Area(RawLocation rawLocation) {
        super(rawLocation.getAreaUa(), rawLocation.getAreaEn());
    }

    @Override
    public String formatForSQL() {
        return "(UUID_TO_BIN('%s'),'%s','%s') ".formatted(id, nameEn, nameUa);
    }

}