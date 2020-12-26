package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.model.enums.AgeRange;
import com.hillel.items_exchange.model.enums.DealType;
import com.hillel.items_exchange.model.enums.Gender;
import com.hillel.items_exchange.model.enums.Lang;
import com.hillel.items_exchange.model.enums.Season;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdvertisementDtoCreatingUtil {

    private static final ImageDto GIF = new ImageDto(0L, "test image gif".getBytes());
    private static final ImageDto JPEG = new ImageDto(1L, "test image jpeg".getBytes());
    private static final ImageDto PNG = new ImageDto(2L, "test image png".getBytes());
    private static final LocationDto KYIV = new LocationDto(0L, "Kyivska", "District", "Kyiv", Lang.EN);
    private static final LocationDto KHARKIV = new LocationDto(1L, "Kharkivska", "Kharkivska district", "Kharkiv", Lang.EN);
    private static final LocationDto ODESSA = new LocationDto(0L, "Odesska", "Odessa district", "Odessa", Lang.EN);
    private static final LocationDto CHANGEDLOCATION =
            new LocationDto(1L, "Kyivska", "New Vasyuki district", "New Vasyuki", Lang.EN);

    public static AdvertisementDto createNonExistAdvertisementDto() {
        return getBuild(0L, "topic", "description", "hat",false, DealType.GIVEAWAY,
                KYIV, AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L, null
                , Collections.singletonList(GIF));
    }

    public static AdvertisementDto createExistAdvertisementDto() {
        return getBuild(1L, "topic", "description", "shoes", true, DealType.EXCHANGE,
                KHARKIV, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 1L, null, Arrays.asList(JPEG, PNG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory() {
        return getBuild(1L, "new topic", "new description", "BMW",true, DealType.EXCHANGE,
                ODESSA, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.FEMALE, "50", 2L, null, Collections.singletonList(JPEG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithUpdatedLocationChangedImagesAndSubcategory() {
        return getBuild(1L, "new topic", "new description", "BMW",true, DealType.EXCHANGE,
                CHANGEDLOCATION, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.FEMALE, "50", 2L, null, Collections.singletonList(JPEG));
    }

    private static AdvertisementDto getBuild(long aId, String topic, String description, String wishes, boolean offer,
                                             DealType exchange, LocationDto city, AgeRange age, Season season,
                                             Gender gender, String size, long subcatId, MultipartFile defaultPhoto, List<ImageDto> images) {
        return AdvertisementDto.builder()
                .id(aId)
                .topic(topic)
                .description(description)
                .wishesToExchange(wishes)
                .readyForOffers(offer)
                .dealType(exchange)
                .location(city)
                .age(age)
                .season(season)
                .gender(gender)
                .size(size)
                .subcategoryId(subcatId)
                .defaultPhoto(defaultPhoto)
                .images(images)
                .build();
    }
}
