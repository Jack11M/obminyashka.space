package space.obminyashka.items_exchange.rest.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;

@Setter
@Getter
public class LocationRaw {
    @JsonSetter(nulls = Nulls.SKIP)
    private String areaUa = "";
    @JsonSetter(nulls = Nulls.SKIP)
    private String districtUa = "";
    @JsonSetter(nulls = Nulls.SKIP)
    private String cityUa = "";
    @JsonSetter(nulls = Nulls.SKIP)
    private String areaEn = "";
    @JsonSetter(nulls = Nulls.SKIP)
    private String districtEn = "";
    @JsonSetter(nulls = Nulls.SKIP)
    private String cityEn = "";

    public boolean equalsTo(@NotNull BaseLocation baseLocation) {
        boolean isAreaEquals = baseLocation.getNameEn().equals(areaEn);
        boolean isDistrictEquals = baseLocation.getNameEn().equals(districtEn);
        return isAreaEquals || isDistrictEquals;
    }
}
