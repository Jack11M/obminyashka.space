package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.LocationRepository;
import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.model.Location;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static space.obminyashka.items_exchange.mapper.UtilMapper.convertTo;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.NEW_VALID_CITY;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.NEW_VALID_DISTRICT;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LocationServiceTest {
    @MockBean
    private LocationRepository locationRepository;
    @Autowired
    private LocationService locationService;
    private Location location;
    private LocationDto locationDto;

    @Captor
    private ArgumentCaptor<Location> locationCaptor;

    @BeforeEach
    void setUp() {
        location = new Location(UUID.randomUUID(), "Kharkivska", "Kharkivska district", "Kharkiv", Locale.ENGLISH.getLanguage(), Collections.emptyList());
        locationDto = convertTo(location, LocationDto.class);
    }

    @Test
    void findAll_shouldReturnAllLocationDtos() {
        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<LocationDto> all = locationService.findAll();
        assertAll("Checking objects' data equal",
                () -> assertEquals(1, all.size()),
                () -> assertEquals(location.getId(), all.get(0).getId()),
                () -> assertEquals(location.getCity(), all.get(0).getCity()),
                () -> assertEquals(location.getDistrict(), all.get(0).getDistrict()));
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnLocation() {
        when(locationRepository.findById(any())).thenReturn(Optional.of(location));

        locationService.getById(UUID.randomUUID()).ifPresent(foundLocation -> assertAll("Checking objects' data equal",
                () -> assertEquals(location.getId(), foundLocation.getId()),
                () -> assertEquals(location.getCity(), foundLocation.getCity()),
                () -> assertEquals(location.getDistrict(), foundLocation.getDistrict())));
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
                () -> assertEquals(locationCaptor.getValue().getCity(), location.getCity()),
                () -> assertEquals(locationCaptor.getValue().getDistrict(), location.getDistrict()));
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
        location.setCity(NEW_VALID_CITY);
        location.setDistrict(NEW_VALID_DISTRICT);
        locationDto.setCity(NEW_VALID_CITY);
        locationDto.setDistrict(NEW_VALID_DISTRICT);
        when(locationRepository.saveAndFlush(any())).thenReturn(location);

        LocationDto updatedLocationDto = locationService.update(locationDto);
        verify(locationRepository, times(1)).saveAndFlush(locationCaptor.capture());
        assertAll("Checking objects' data equal",
                () -> assertEquals(locationCaptor.getValue().getId(), updatedLocationDto.getId()),
                () -> assertEquals(locationCaptor.getValue().getCity(), updatedLocationDto.getCity()),
                () -> assertEquals(locationCaptor.getValue().getDistrict(), updatedLocationDto.getDistrict()));
    }
}
