package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;
import space.obminyashka.items_exchange.rest.request.LocationRaw;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class City extends BaseLocation {

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    public City(LocationRaw locationRaw, District district) {
        super(locationRaw.getCityUa(), locationRaw.getCityEn());
        this.district = district;
    }

    @Override
    public String formatForSQL() {
        return " (UUID_TO_BIN('%s'),UUID_TO_BIN('%s'),'%s','%s')".formatted(id, district.getId(), nameEn, nameUa);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;
        if (!super.equals(o)) return false;

        return district.equals(city.district);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + district.hashCode();
        return result;
    }
}
