package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.model.enums.I18n;

public class LocationDtoCreatingUtil {
    public static final String NEW_VALID_CITY = "Kyiv";
    public static final String NEW_INVALID_CITY = "K";
    public static final String NEW_VALID_DISTRICT = "Podolsky";
    public static final String NEW_VALID_AREA = "Kyivska";

    public static LocationDto createLocationDtoWithId(long id) {
        return getBuild(id, NEW_VALID_CITY, NEW_VALID_DISTRICT, NEW_VALID_AREA, I18n.EN);
    }

    public static LocationDto createLocationDtoForCreatingWithInvalidCity() {
        return getBuild(1L, NEW_INVALID_CITY, NEW_VALID_DISTRICT, NEW_VALID_AREA, I18n.EN);
    }

    public static LocationDto getBuild(long id, String city, String district, String area, I18n i18N) {
        return LocationDto.builder()
                .id(id)
                .city(city)
                .district(district)
                .area(area)
                .i18N(i18N)
                .build();
    }
}
