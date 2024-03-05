package space.obminyashka.items_exchange.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import space.obminyashka.items_exchange.repository.enums.DealType;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.repository.enums.Season;
import space.obminyashka.items_exchange.rest.dto.LocationDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementDisplayView {

    private UUID advertisementId;
    private String age;
    @JsonProperty("sizeValue")
    private String size;
    private String topic;
    private String phone;
    private Season season;
    private Gender gender;
    private String ownerName;
    private DealType dealType;
    private byte[] ownerAvatar;
    private String createdDate;
    private String description;
    private LocationDto location;
    private List<ImageView> images;
    private boolean readyForOffers;
    private String wishesToExchange;
    private CategoryNameView category;
    private SubcategoryView subcategory;
}
