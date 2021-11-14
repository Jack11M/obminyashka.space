package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.LocationDto;

import java.util.Locale;

public class LocationDtoCreatingUtil {
    public static final String NEW_VALID_CITY = "Kyiv";
    public static final String NEW_INVALID_CITY = "K";
    public static final String NEW_VALID_DISTRICT = "Podolsky";
    public static final String NEW_VALID_AREA = "Kyivska";

    public static LocationDto createLocationDtoWithId(long id) {
        return getBuild(id, NEW_VALID_CITY, NEW_VALID_DISTRICT, NEW_VALID_AREA, Locale.ENGLISH.getLanguage());
    }

    public static LocationDto createLocationDtoForCreatingWithInvalidCity() {
        return getBuild(1L, NEW_INVALID_CITY, NEW_VALID_DISTRICT, NEW_VALID_AREA, Locale.ENGLISH.getLanguage());
    }

    public static LocationDto getBuild(long id, String city, String district, String area, String i18N) {
        return LocationDto.builder()
                .id(id)
                .city(city)
                .district(district)
                .area(area)
                .i18N(i18N)
                .build();
    }
}
