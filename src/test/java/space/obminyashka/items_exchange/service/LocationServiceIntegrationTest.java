package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.LocationRepository;
import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.mapper.LocationMapper;
import space.obminyashka.items_exchange.model.Location;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LocationServiceIntegrationTest {
    @MockBean
    private LocationRepository locationRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationMapper locationMapper;
    private Location location;
    private LocationDto locationDto;

    @Captor
    private ArgumentCaptor<Location> locationCaptor;

    @BeforeEach
    void setUp() {
        location = new Location(UUID.randomUUID(), "Харківська", "Харківська область", "Харків", "Kharkivska", "Kharkivska district", "Kharkiv", Collections.emptyList());
        locationDto = locationMapper.toDto(location);
    }

    @Test
    void findAll_shouldReturnAllLocationDtos() {
        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<LocationDto> all = locationService.findAll();
        assertAll("Checking objects' data equal",
                () -> assertEquals(1, all.size()),
                () -> assertEquals(location.getId(), all.get(0).getId()),
                () -> assertEquals(location.getCityUA(), all.get(0).getCityUA()),
                () -> assertEquals(location.getDistrictUA(), all.get(0).getDistrictUA()),
                () -> assertEquals(location.getAreaUA(), all.get(0).getAreaUA()),
                () -> assertEquals(location.getCityEN(), all.get(0).getCityEN()),
                () -> assertEquals(location.getDistrictEN(), all.get(0).getDistrictEN()),
                () -> assertEquals(location.getAreaEN(), all.get(0).getAreaEN()));
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnLocation() {
        when(locationRepository.findById(any())).thenReturn(Optional.of(location));

        locationService.getById(UUID.randomUUID()).ifPresent(foundLocation -> assertAll("Checking objects' data equal",
                () -> assertEquals(location.getId(), foundLocation.getId()),
                () -> assertEquals(location.getCityUA(), foundLocation.getCityUA()),
                () -> assertEquals(location.getDistrictUA(), foundLocation.getDistrictUA()),
                () -> assertEquals(location.getAreaUA(), foundLocation.getAreaUA()),
                () -> assertEquals(location.getCityEN(), foundLocation.getCityEN()),
                () -> assertEquals(location.getDistrictEN(), foundLocation.getDistrictEN()),
                () -> assertEquals(location.getAreaEN(), foundLocation.getAreaEN())));
        verify(locationRepository).findById(any());
    }

    @Test
    void getById_shouldReturn404_WhenEntityNotFound() {
        when(locationRepository.findById(any())).thenReturn(Optional.empty());

        final var id = UUID.randomUUID();
        Optional<LocationDto> optionalLocationDto = locationService.getById(id);
        assertTrue(optionalLocationDto.isEmpty());
        verify(locationRepository, times(1)).findById(id);
    }

    @Test
    void save_shouldSaveLocation_WithLocationParameter() {
        saveLocationBasicTest();
    }

    private void saveLocationBasicTest() {
        locationService.save(location);
        verify(locationRepository, times(1)).save(locationCaptor.capture());
        assertAll("Checking objects' data equal",
                () -> assertEquals(locationCaptor.getValue().getCityUA(), location.getCityUA()),
                () -> assertEquals(locationCaptor.getValue().getDistrictUA(), location.getDistrictUA()));
    }

    @Test
    void save_shouldSaveLocation_WithLocationDtoParameter() {
        when(locationRepository.saveAndFlush(any())).thenReturn(location);

        saveLocationBasicTest();
    }

    @Test
    void removeById_shouldRemoveOneLocationWithGivenId() {
        var locationId = UUID.randomUUID();
        locationService.removeById(locationId);
        verify(locationRepository).deleteById(locationId);
        verifyNoMoreInteractions(locationRepository);
    }

    @Test
    void removeById_shouldRemoveAllLocationsWithGivenId() {
        List<UUID> locationIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        locationService.removeById(locationIds);
        verify(locationRepository, times(locationIds.size())).deleteById(any());
        verifyNoMoreInteractions(locationRepository);
    }

    @Test
    void existsById_shouldReturnTrueIfLocationsWithGivenIdExists() {
        when(locationRepository.existsById(any())).thenReturn(true);
        final var id = UUID.randomUUID();
        boolean existsById = locationService.existsById(id);
        verify(locationRepository, times(1)).existsById(id);
        assertTrue(existsById);
    }

    @Test
    void update_shouldUpdateLocation() {
        locationDto.setDistrictUA("Sumska");
        when(locationRepository.saveAndFlush(any())).thenAnswer(i -> i.getArguments()[0]);

        LocationDto updatedLocationDto = locationService.update(locationDto);
        verify(locationRepository).saveAndFlush(any());
        assertEquals(locationDto, updatedLocationDto);
    }
}
