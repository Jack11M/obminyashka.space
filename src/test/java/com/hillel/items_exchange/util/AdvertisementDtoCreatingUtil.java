package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.dto.ProductDto;
import com.hillel.items_exchange.model.DealType;

import java.util.Arrays;
import java.util.Collections;

public class AdvertisementDtoCreatingUtil {
    public static AdvertisementDto createNonExistAdvertisementDto() {
        LocationDto kyiv = new LocationDto(0L, "Kyiv", "District");
        ProductDto springDress = new ProductDto(0L, "16", "male", "spring", "M", 1L,
                Collections.singletonList(new ImageDto(0L, "test image gif".getBytes(), false)));
        return(new AdvertisementDto(0L, "topic", "description", "hat", false, false, DealType.GIVEAWAY, kyiv, springDress));
    }

    public static AdvertisementDto createExistAdvertisementDto() {
        LocationDto kharkiv = new LocationDto(1L, "Kharkiv", "Kharkivska district");
        ProductDto springDress = new ProductDto(1L, "16", "male", "spring", "40", 1L,
                Arrays.asList(
                        new ImageDto(1L, "test image jpeg".getBytes(), false),
                        new ImageDto(2L, "test image png".getBytes(), true)));
        return(new AdvertisementDto(1L, "topic", "description", "shoes", true, true, DealType.EXCHANGE, kharkiv, springDress));
    }

    public static AdvertisementDto createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory() {
        LocationDto odessa = new LocationDto(0L, "Odessa", "Odessa district");
        ProductDto springDress = new ProductDto(1L, "16", "female", "spring", "50", 2L,
                Collections.singletonList(new ImageDto(1L, "test image jpeg".getBytes(), false)));
        return(new AdvertisementDto(1L, "new topic", "new description",
                "BMW", true, true, DealType.EXCHANGE, odessa, springDress));
    }
}
