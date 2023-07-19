package space.obminyashka.items_exchange.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationNameView {
    private UUID id;
    private String nameUa;
    private String nameEn;
}
