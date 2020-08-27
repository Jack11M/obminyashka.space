package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.LocationRepository;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.hillel.items_exchange.mapper.UtilMapper.convertTo;
import static com.hillel.items_exchange.util.LocationDtoCreatingUtil.NEW_VALID_CITY;
import static com.hillel.items_exchange.util.LocationDtoCreatingUtil.NEW_VALID_DISTRICT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        location = new Location(1L, "Kharkiv", "Kharkivska district", null);
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
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        Location foundLocation = locationService.findById(1L).get();
        assertAll("Checking objects' data equal",
                () -> assertEquals(location.getId(), foundLocation.getId()),
                () -> assertEquals(location.getCity(), foundLocation.getCity()),
                () -> assertEquals(location.getDistrict(), foundLocation.getDistrict()));
        verify(locationRepository, times(1)).findById(anyLong());
    }

    @Test
    void getById_shouldReturnLocationDto() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        LocationDto foundLocation = locationService.getById(1L).get();
        assertAll("Checking objects' data equal",
                () -> assertEquals(location.getId(), foundLocation.getId()),
                () -> assertEquals(location.getCity(), foundLocation.getCity()),
                () -> assertEquals(location.getDistrict(), foundLocation.getDistrict()));
        verify(locationRepository, times(1)).findById(anyLong());
    }

    @Test
    void getById_shouldReturn404_WhenEntityNotFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<LocationDto> optionalLocationDto = locationService.getById(55L);
        assertTrue(optionalLocationDto.isEmpty());
        verify(locationRepository, times(1)).findById(55L);
    }

    @Test
    void save_shouldSaveLocation_WithLocationParameter() {
        locationService.save(location);
        verify(locationRepository, times(1)).save(locationCaptor.capture());
        assertAll("Checking objects' data equal",
                () -> assertEquals(locationCaptor.getValue().getCity(), location.getCity()),
                () -> assertEquals(locationCaptor.getValue().getDistrict(), location.getDistrict()));
    }

    @Test
    void save_shouldSaveLocation_WithLocationDtoParameter() {
        when(locationRepository.saveAndFlush(any())).thenReturn(location);

        locationService.save(locationDto);
        verify(locationRepository, times(1)).saveAndFlush(locationCaptor.capture());
        assertAll("Checking objects' data equal",
                () -> assertEquals(locationCaptor.getValue().getCity(), locationDto.getCity()),
                () -> assertEquals(locationCaptor.getValue().getDistrict(), locationDto.getDistrict()));
    }

    @Test
    void removeById_shouldRemoveOneLocationWithGivenId() {
        long locationId = 1L;
        locationService.removeById(locationId);
        verify(locationRepository).deleteById(locationId);
        verifyNoMoreInteractions(locationRepository);
    }

    @Test
    void removeById_shouldRemoveAllLocationsWithGivenId() {
        List<Long> locationIds = List.of(1L, 2L, 3L);
        locationService.removeById(locationIds);
        verify(locationRepository, times(locationIds.size())).deleteById(anyLong());
        verifyNoMoreInteractions(locationRepository);
    }

    @Test
    void existsById_shouldReturnTrueIfLocationsWithGivenIdExists() {
        when(locationRepository.existsById(anyLong())).thenReturn(true);
        boolean existsById = locationService.existsById(1L);
        verify(locationRepository, times(1)).existsById(1L);
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