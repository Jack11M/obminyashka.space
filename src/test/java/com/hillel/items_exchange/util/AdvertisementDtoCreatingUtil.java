package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.dto.ProductDto;
import com.hillel.items_exchange.model.DealType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdvertisementDtoCreatingUtil {
    private static final String MALE = "male";
    private static final String FEMALE = "female";
    private static final ImageDto GIF = new ImageDto(0L, "test image gif".getBytes(), false);
    private static final ImageDto JPEG = new ImageDto(1L, "test image jpeg".getBytes(), false);
    private static final ImageDto PNG = new ImageDto(2L, "test image png".getBytes(), true);
    private static final LocationDto KYIV = new LocationDto(0L, "Kyiv", "District");
    private static final LocationDto KHARKIV = new LocationDto(1L, "Kharkiv", "Kharkivska district");
    private static final LocationDto ODESSA = new LocationDto(0L, "Odessa", "Odessa district");


    public static AdvertisementDto createNonExistAdvertisementDto() {
        return getBuild(0L,"topic", "description", "hat", false, false, DealType.GIVEAWAY,
                KYIV, 0L, MALE, "M", 1L, Collections.singletonList(GIF));
    }

    public static AdvertisementDto createExistAdvertisementDto() {
        return getBuild(1L,"topic", "description", "shoes", true, true, DealType.EXCHANGE,
                KHARKIV, 1L, MALE, "40", 1L, Arrays.asList(JPEG, PNG));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory() {
        return getBuild(1L, "new topic", "new description", "BMW", true, true, DealType.EXCHANGE,
                ODESSA, 1L, FEMALE, "50", 2L, Collections.singletonList(JPEG));
    }

    private static AdvertisementDto getBuild(long aId, String topic, String description, String wishes, boolean offer, boolean fav, DealType exchange,
                                             LocationDto city, long pId, String gender, String size, long subcatId, List<ImageDto> images) {
        return AdvertisementDto.builder()
                .id(aId)
                .topic(topic)
                .description(description)
                .wishesToExchange(wishes)
                .readyForOffers(offer)
                .isFavourite(fav)
                .dealType(exchange)
                .location(city)
                .product(new ProductDto(pId, "16", gender, "spring", size, subcatId, images))
                .build();
    }
}
