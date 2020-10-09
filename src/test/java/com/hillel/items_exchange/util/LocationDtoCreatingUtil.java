package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.LocationDto;

public class LocationDtoCreatingUtil {
    public static final String NEW_VALID_CITY = "Kyiv";
    public static final String NEW_INVALID_CITY = "K";
    public static final String NEW_VALID_DISTRICT = "Podolsky";

    public static LocationDto createLocationDtoWithId(long id) {
        return getBuild(id, NEW_VALID_CITY, NEW_VALID_DISTRICT);
    }

    public static LocationDto createLocationDtoForCreatingWithInvalidCity() {
        return getBuild(1L, NEW_INVALID_CITY, NEW_VALID_DISTRICT);
    }

    public static LocationDto getBuild(long id, String city, String district) {
        return LocationDto.builder()
                .id(id)
                .city(city)
                .district(district)
                .build();
    }
}
