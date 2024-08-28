package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class City extends BaseLocation {

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;
        if (!super.equals(o)) return false;

        return area.equals(city.area);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + area.hashCode();
        return result;
    }
}
