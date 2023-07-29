package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.repository.AreaRepository;
import space.obminyashka.items_exchange.repository.model.Area;
import space.obminyashka.items_exchange.rest.mapper.LocationMapper;
import space.obminyashka.items_exchange.rest.response.LocationNameView;
import space.obminyashka.items_exchange.service.impl.LocationServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Mock
    private AreaRepository areaRepository;
    @InjectMocks
    private LocationServiceImpl locationService;
    @Mock
    private LocationMapper locationMapper;
    private Area area;
    private LocationNameView areaNameView;

    @BeforeEach
    void setUp() {
        area = new Area(UUID.randomUUID(), "Одеська", "Odeska");
        areaNameView = new LocationNameView(area.getId(), area.getNameUa(), area.getNameEn());
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
}
