package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementDisplayDto {

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
    private List<ImageDto> images;
    private boolean readyForOffers;
    private String wishesToExchange;
    private CategoryNameDto category;
    private SubcategoryDto subcategory;
}
