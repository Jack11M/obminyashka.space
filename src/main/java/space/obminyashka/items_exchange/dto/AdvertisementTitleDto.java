package space.obminyashka.items_exchange.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"advertisementId"})
public class AdvertisementTitleDto {
    private long advertisementId;
    private byte[] image;
    private String title;
    private LocationDto location;
    private String ownerName;
    private byte[] ownerAvatar;
}
