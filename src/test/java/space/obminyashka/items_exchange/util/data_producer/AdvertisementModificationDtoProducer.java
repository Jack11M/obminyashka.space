package space.obminyashka.items_exchange.util.data_producer;

import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.repository.enums.AgeRange;
import space.obminyashka.items_exchange.repository.enums.DealType;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.repository.enums.Season;

import java.util.UUID;

public class AdvertisementModificationDtoProducer {

    private static final UUID existedLocationId = UUID.fromString("2c5467f3-b7ee-48b1-9451-7028255b757b");
    private static final UUID existedAdvertisementId = UUID.fromString("65e3ee49-5927-40be-aafd-0461ce45f295");
    private static final String NOT_VALID_DESCRIPTION = createString(256);
    private static final String NOT_VALID_WISHES = createString(211);
    private static final String NOT_VALID_SIZE = createString(0);
    private static final String NOT_VALID_TOPIC = createString(2);

    public static AdvertisementModificationDto createNonExistAdvertisementModificationDto() {
        return getBuild(null,"unique topic", "unique description", "house",false, DealType.GIVEAWAY,
                AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "57 - 62", 1L);
    }

    public static AdvertisementModificationDto createNonExistAdvertisementModificationDtoWithBlankFields() {
        return getBuild(null,"     ", "     ", "     ",false, DealType.GIVEAWAY,
                AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L);
    }

    public static AdvertisementModificationDto createNonExistAdvDtoWithBlankWishAndTrueReadyForOffer() {
        return getBuild(null,"     ", "     ", "     ",true, DealType.GIVEAWAY,
                AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L);
    }

    public static AdvertisementModificationDto createNonExistAdvDtoWithNotBlankWishAndTrueReadyForOffer() {
        return getBuild(null,"     ", "     ", "some",true, DealType.GIVEAWAY,
                AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L);
    }

    public static AdvertisementModificationDto createNonExistAdvDtoWithNotBlankWishAndFalseReadyForOffer() {
        return getBuild(null,"     ", "     ", "some",false, DealType.GIVEAWAY,
                AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L);
    }

    public static AdvertisementModificationDto createExistAdvertisementModificationDto() {
        return getBuild(existedAdvertisementId, "topic", "description", "shoes", true, DealType.EXCHANGE,
                AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "57 - 62", 2L);
    }

    public static AdvertisementModificationDto createExistAdvertisementModificationDtoForUpdate() {
        return getBuild(existedAdvertisementId, "new topic", "new description", "BMW",true, DealType.EXCHANGE,
                AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.FEMALE, "57 - 62", 2L);
    }

    public static AdvertisementModificationDto createExistAdvertisementDtoForUpdateWithNotValidFields() {
        return getBuild(existedAdvertisementId, NOT_VALID_TOPIC, NOT_VALID_DESCRIPTION, NOT_VALID_WISHES, true, DealType.EXCHANGE,
                AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, NOT_VALID_SIZE, 1L);
    }

    private static AdvertisementModificationDto getBuild(UUID advId, String topic, String description, String wishes,
                                                         boolean offer, DealType exchange, AgeRange age,
                                                         Season season, Gender gender, String size, long subcatId) {
        return AdvertisementModificationDto.builder()
                .id(advId)
                .age(age)
                .size(size)
                .topic(topic)
                .gender(gender)
                .season(season)
                .dealType(exchange)
                .readyForOffers(offer)
                .locationId(AdvertisementModificationDtoProducer.existedLocationId)
                .subcategoryId(subcatId)
                .wishesToExchange(wishes)
                .description(description)
                .build();
    }

    private static String createString(int quantityOfCharsInNewsString){
        return "x".repeat(Math.max(0, quantityOfCharsInNewsString));
    }
}
