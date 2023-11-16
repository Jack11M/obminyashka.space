package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;
import space.obminyashka.items_exchange.rest.request.LocationRaw;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class District extends BaseLocation {

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    public District(LocationRaw locationRaw, Area area) {
        super(locationRaw.getDistrictUa(), locationRaw.getDistrictEn());
        this.area = area;
    }

    public String formatForSQL() {
        return " (UUID_TO_BIN('%s'),UUID_TO_BIN('%s'),'%s','%s')".formatted(id, area.getId(), nameEn, nameUa);
    }
}
