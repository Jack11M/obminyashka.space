package space.obminyashka.items_exchange.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"advertisementId"})
public class AdvertisementTitleDto {
    private UUID advertisementId;
    private byte[] image;
    private String title;
    private LocationDto location;
    private String ownerName;
    private byte[] ownerAvatar;
    private boolean enableRandom = true;
}
