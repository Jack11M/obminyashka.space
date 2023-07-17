package space.obminyashka.items_exchange.rest.response;

import lombok.*;
import space.obminyashka.items_exchange.rest.dto.LocationDto;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"advertisementId"})
public class AdvertisementTitleView {
    private UUID advertisementId;
    private byte[] image;
    private String title;
    private LocationDto location;
}
