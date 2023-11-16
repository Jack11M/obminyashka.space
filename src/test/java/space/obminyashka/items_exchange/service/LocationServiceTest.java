package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.repository.AreaRepository;
import space.obminyashka.items_exchange.repository.DistrictRepository;
import space.obminyashka.items_exchange.repository.model.Area;
import space.obminyashka.items_exchange.repository.model.District;
import space.obminyashka.items_exchange.rest.mapper.LocationMapper;
import space.obminyashka.items_exchange.rest.request.LocationRaw;
import space.obminyashka.items_exchange.rest.response.LocationNameView;
import space.obminyashka.items_exchange.service.impl.LocationServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Mock
    private AreaRepository areaRepository;
    @Mock
    private DistrictRepository districtRepository;
    @InjectMocks
    private LocationServiceImpl locationService;
    @Mock
    private LocationMapper locationMapper;
    private Area area;
    private District district;
    private LocationNameView areaNameView;
    private LocationNameView districtNameView;

    @BeforeEach
    void setUp() {
        LocationRaw locationRaw = new LocationRaw();
        locationRaw.setAreaEn("Odeska");
        locationRaw.setAreaUa("Одеська");
        locationRaw.setDistrictEn("Limanskii district");
        locationRaw.setDistrictUa("Лиманський район");
        area = new Area(locationRaw);
        areaNameView = new LocationNameView(area.getId(), area.getNameUa(), area.getNameEn());
        district = new District(locationRaw,area);
        districtNameView = new LocationNameView(district.getId(), district.getNameUa(), district.getNameEn());
    }
    @Test
    void findAll_shouldReturnAllLocationNameViewOfAreas() {
        var expectedAllAreasName = List.of(areaNameView);
        var expectedAllAreas = List.of(area);
        when(areaRepository.findAll()).thenReturn(expectedAllAreas);
        when(locationMapper.toNameViewList(expectedAllAreas)).thenReturn(expectedAllAreasName);

        List<LocationNameView> actualAreas = locationService.findAllAreas();

        assertAll("Checking objects' data equal",
                () -> assertArrayEquals(expectedAllAreasName.toArray(), actualAreas.toArray()),
                () -> verify(locationMapper).toNameViewList(expectedAllAreas),
                () -> verify(areaRepository).findAll());
    }

    @Test
    void findAllDistricts_shouldReturnAllDistrictsLocationNameViewByAreaId() {
        var expectedAllDistrictsName = List.of(districtNameView);
        var expectedAllDistricts = List.of(district);
        var expectedAreaId = area.getId();
        when(districtRepository.findAllByAreaId(expectedAreaId)).thenReturn(expectedAllDistricts);
        when(locationMapper.toDistrictNameViewList(expectedAllDistricts)).thenReturn(expectedAllDistrictsName);

        List<LocationNameView> actualDistricts = locationService.findAllDistrictsByAreaId(expectedAreaId);

        assertAll(
                () -> assertArrayEquals(expectedAllDistrictsName.toArray(), actualDistricts.toArray()),
                () -> verify(locationMapper).toDistrictNameViewList(expectedAllDistricts),
                () -> verify(districtRepository).findAllByAreaId(expectedAreaId)
        );
    }
}
