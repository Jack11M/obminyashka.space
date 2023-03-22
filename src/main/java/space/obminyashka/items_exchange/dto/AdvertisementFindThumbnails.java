package space.obminyashka.items_exchange.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AdvertisementFindThumbnails {

    private int page;

    private int size;

    private Long subcategoryId;

    private UUID excludeAdvertisementId;

}
