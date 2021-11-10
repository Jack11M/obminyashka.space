package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dao.LocationRepository;
import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.exception.InvalidLocationInitFileCreatingDataException;
import space.obminyashka.items_exchange.model.Location;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static space.obminyashka.items_exchange.mapper.UtilMapper.convertTo;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.NEW_VALID_CITY;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.NEW_VALID_DISTRICT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LocationServiceTest {
    @MockBean
    private LocationRepository locationRepository;
    @Autowired
    private LocationService locationService;
    private Location location;
    private LocationDto locationDto;

    @Value("${test.data.location.init.file.path}")
    private String pathToFileParseLocationsFrom;
    @Value("${location.init.file.path}")
    private String pathToCreateLocationsInitFile;

    @Captor
    private ArgumentCaptor<Location> locationCaptor;

    @BeforeEach
    void setUp() {
        location = new Location(1L, "Kharkivska", "Kharkivska district", "Kharkiv", Locale.ENGLISH.getLanguage(), Collections.emptyList());
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

        locationService.getById(1L).ifPresent(foundLocation -> assertAll("Checking objects' data equal",
                () -> assertEquals(location.getId(), foundLocation.getId()),
                () -> assertEquals(location.getCity(), foundLocation.getCity()),
                () -> assertEquals(location.getDistrict(), foundLocation.getDistrict())));
        verify(locationRepository).findById(anyLong());
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

    @Test
    void createFileToInitLocations_shouldReturnProperData()
            throws IOException, InvalidLocationInitFileCreatingDataException {
        String locationsString = Files.readString(Path.of(pathToFileParseLocationsFrom), StandardCharsets.UTF_8);
        String createdFileContent = locationService.createParsedLocationsFile(locationsString);
        String[] parsedLocationsQuantity = createdFileContent.split("\\), \\(");
        int lastId = Integer.parseInt(parsedLocationsQuantity[parsedLocationsQuantity.length - 1].substring(1, 5));
        int locationsInThreeLanguages = locationsString.split("},\\{").length;
        assertEquals(locationsInThreeLanguages * 3, lastId);
    }

    @Test
    void createFileToInitLocations_shouldCreateNotEmptyFileToInitLocationsInDB()
            throws IOException, InvalidLocationInitFileCreatingDataException {
        locationService.createParsedLocationsFile(Files.readString(Path.of(pathToFileParseLocationsFrom), StandardCharsets.UTF_8));
        assertTrue(Files.size(Path.of(pathToCreateLocationsInitFile)) > 0);
    }

    @Test
    void isLocationDataValid_whenDataIsValid_shouldReturnTrue() throws IOException {
        assertTrue(locationService.isLocationDataValid(Files.readString(Path.of(pathToFileParseLocationsFrom))));
    }

    @Test
    void isLocationDataValid_whenDataIsNotValid_shouldReturnFalse() {
        assertFalse(locationService.isLocationDataValid("INVALID DATA"));
    }
}
