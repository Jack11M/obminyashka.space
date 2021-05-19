package space.obminyashka.items_exchange.dto;

import java.util.List;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import space.obminyashka.items_exchange.mapper.transfer.Exist;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementDisplayDto {

    @Positive(groups = Exist.class, message = "{invalid.exist.id}")
    private long advertisementId;
    private String age;
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
