package space.obminyashka.items_exchange.dto;

import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.model.Location;

@Setter
@Getter
public class RawLocation {
    private Location en;
    private Location ua;
    private Location ru;
}
