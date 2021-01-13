package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.model.Location;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AdvertisementTitleDto {
    private long advertisementId;
    private byte[] image;
    private String title;
    private Location location;
    private String ownerName;
    private byte[] ownerAvatar;
}
