package space.obminyashka.items_exchange.rest.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class RawLocation {
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
}
