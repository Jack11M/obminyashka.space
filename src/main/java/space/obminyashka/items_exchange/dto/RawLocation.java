package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RawLocation {
    @JsonSetter(nulls = Nulls.SKIP)
    private String fullAddressUa = "";
    @JsonSetter(nulls = Nulls.SKIP)
    private String fullAddressEn = "";
}
