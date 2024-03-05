package space.obminyashka.items_exchange.util.data_producer;

import space.obminyashka.items_exchange.rest.dto.LocationDto;

public class LocationDtoProducer {
    public static final String CITY_UA = "Київ";
    public static final String DISTRICT_UA = "Подільський район";
    public static final String AREA_UA = "Київська область";
    public static final String CITY_EN = "Kyiv";
    public static final String DISTRICT_EN = "Podolsky district";
    public static final String AREA_EN = "Kyivska area";

    public static LocationDto createValidLocationDto() {
        return LocationDto.builder()
                .cityUA(CITY_UA)
                .districtUA(DISTRICT_UA)
                .areaUA(AREA_UA)
                .cityEN(CITY_EN)
                .districtEN(DISTRICT_EN)
                .areaEN(AREA_EN)
                .build();
    }

    public static LocationDto createLocationDtoForCreatingWithInvalidCity() {
        return LocationDto.builder()
                .cityUA(CITY_UA)
                .districtUA(DISTRICT_UA)
                .areaUA(AREA_UA)
                .cityEN(null)
                .districtEN(DISTRICT_EN)
                .areaEN(AREA_EN)
                .build();
    }
}
