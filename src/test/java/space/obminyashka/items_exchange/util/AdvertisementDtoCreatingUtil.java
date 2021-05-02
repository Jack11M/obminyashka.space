package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.AdvertisementDto;
import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.I18n;
import space.obminyashka.items_exchange.model.enums.Season;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdvertisementDtoCreatingUtil {

    private static final ImageDto GIF = new ImageDto(0L, "test image gif".getBytes());
    private static final ImageDto JPEG = new ImageDto(1L, "test image jpeg".getBytes());
    private static final ImageDto PNG = new ImageDto(2L, "test image png".getBytes());
    private static final LocationDto KYIV = new LocationDto(0L, "Kyivska", "District", "Kyiv", I18n.EN);
    private static final LocationDto KHARKIV = new LocationDto(1L, "Kharkivska", "Kharkivska district", "Kharkiv", I18n.EN);
    private static final LocationDto ODESSA = new LocationDto(0L, "Odesska", "Odessa district", "Odessa", I18n.EN);
    private static final LocationDto CHANGEDLOCATION =
            new LocationDto(1L, "Kyivska", "New Vasyuki district", "New Vasyuki", I18n.EN);

    private static final LocationDto NOT_VALID_LOCATION =
            new LocationDto(2L, "b", "b", "b", I18n.EN);
    private static final String NOT_VALID_DESCRIPTION = createString(256);
    private static final String NOT_VALID_WISHES = createString(211);

    public static AdvertisementDto createNonExistAdvertisementDto() {
        return getBuild(0L, "topic", "description", "hat",false, DealType.GIVEAWAY,
                KYIV, AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L, Collections.singletonList(GIF));
    }

    public static AdvertisementDto createExistAdvertisementDto() {
        return getBuild(1L, "topic", "description", "shoes", true, DealType.EXCHANGE,
                KHARKIV, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 1L, Arrays.asList(JPEG, PNG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory() {
        return getBuild(1L, "new topic", "new description", "BMW",true, DealType.EXCHANGE,
                ODESSA, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.FEMALE, "50", 2L, Collections.singletonList(JPEG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithUpdatedLocationChangedImagesAndSubcategory() {
        return getBuild(1L, "new topic", "new description", "BMW",true, DealType.EXCHANGE,
                CHANGEDLOCATION, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.FEMALE, "50", 2L, Collections.singletonList(JPEG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNotValidSize() {
        return getBuild(1L, "topic", "description", "shoes", true, DealType.EXCHANGE,
                KHARKIV, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "", 1L, Arrays.asList(JPEG, PNG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNotValidLocation() {
        return getBuild(1L, "topic", "description", "shoes", true, DealType.EXCHANGE,
                NOT_VALID_LOCATION, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 1L, Arrays.asList(JPEG, PNG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNotValidTopic() {
        return getBuild(1L, "to", "description", "shoes", true, DealType.EXCHANGE,
                KHARKIV, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 1L, Arrays.asList(JPEG, PNG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNotValidDescription() {
        return getBuild(1L, "topic", NOT_VALID_DESCRIPTION, "shoes", true, DealType.EXCHANGE,
                KHARKIV, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 1L, Arrays.asList(JPEG, PNG));
    }


    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNotValidWishesToExchange() {
        return getBuild(1L, "topic", "description", NOT_VALID_WISHES, true, DealType.EXCHANGE,
                KHARKIV, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 1L, Arrays.asList(JPEG, PNG));
    }

    private static AdvertisementDto getBuild(long aId, String topic, String description, String wishes, boolean offer,
                                             DealType exchange, LocationDto city, AgeRange age, Season season,
                                             Gender gender, String size, long subcatId, List<ImageDto> images) {
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
                .images(images)
                .build();
    }

    private static String createString(int quantityOfCharsInNewsString){
        return "x".repeat(Math.max(0, quantityOfCharsInNewsString));
    }
}
