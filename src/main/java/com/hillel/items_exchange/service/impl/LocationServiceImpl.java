package com.hillel.items_exchange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.items_exchange.dao.LocationRepository;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.exception.InvalidLocationInitFileCreatingDataException;
import com.hillel.items_exchange.model.Location;
import com.hillel.items_exchange.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.mapper.UtilMapper.convertAllTo;
import static com.hillel.items_exchange.mapper.UtilMapper.convertTo;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    public static final String PATH_LOCATION_INIT_FILE = "src/main/resources/sql/fill-table-location.sql";
    public static final String LOCATION_INIT_FILE_CREATE_DATA_PATTERN = "\"[a-z][a-z]\":\"city\":\"[ A-Za-zА-Яа-яЁёЇїІіЄєҐґ'’-]{0,30}\",\"district\":\"[A-Za-zА-Яа-яЁёЇїІіЄєҐґ'’-]{0,30}\",\"area\":\"[A-Za-zА-Яа-яЁёЇїІіЄєҐґ'’-]{0,30}\"";

    private final LocationRepository locationRepository;

    @Override
    public List<LocationDto> findAll() {
        return new ArrayList<>(convertAllTo(locationRepository.findAll(), LocationDto.class, ArrayList::new));
    }

    @Override
    public Optional<Location> findById(long id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<LocationDto> findByIds(List<Long> ids) {
        List<Location> locations = locationRepository.findByIdIn(ids);
        return new ArrayList<>(convertAllTo(locations, LocationDto.class, ArrayList::new));
    }

    @Override
    public Optional<LocationDto> getById(long id) {
        return findById(id)
                .map(location -> convertTo(location, LocationDto.class));
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public LocationDto save(LocationDto locationDto) {
        Location location = convertTo(locationDto, Location.class);
        Location savedLocation = locationRepository.saveAndFlush(location);
        return convertTo(savedLocation, LocationDto.class);
    }

    @Override
    public void removeById(long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void removeById(List<Long> ids) {
        ids.forEach(this::removeById);
    }

    @Override
    public boolean existsById(long id) {
        return locationRepository.existsById(id);
    }

    @Override
    public LocationDto update(LocationDto locationDto) {
        Location location = convertTo(locationDto, Location.class);
        Location updatedLocation = locationRepository.saveAndFlush(location);
        return convertTo(updatedLocation, LocationDto.class);
    }

    @Override
    public String createFileToInitLocations(String creatingData)
            throws IOException, InvalidLocationInitFileCreatingDataException {
        File file = new File(PATH_LOCATION_INIT_FILE);
        List<Location> locations = mapCreatingDataToLocations(creatingData);
        String initString = "INSERT INTO `evo_exchange`.`location` (`id`, `city`, `district`, `area`, `i18n`) VALUES";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.append(initString);
            for (int i = 0; i < locations.size(); i++) {
                Location location = locations.get(i);
                writer.append(String.format(" ('%s', '%s','%s','%s','%s')",
                        i + 1,
                        location.getCity().replace("'", "\\'"),
                        location.getDistrict().replace("'", "\\'"),
                        location.getArea().replace("'", "\\'"),
                        location.getI18n()));
                if (i < locations.size()-1) writer.append(",");
            }
        }
        return Files.readString(Path.of(file.getAbsolutePath()));
    }

    @Override
    public boolean isLocationDataValid(String locationDataString) {
        Pattern pattern = Pattern.compile(LOCATION_INIT_FILE_CREATE_DATA_PATTERN);
        return Arrays.stream(locationDataString.split("},"))
                .map(s -> s.replaceAll("[\\[\\]{}]", ""))
                .allMatch(s -> pattern.matcher(s).matches());
    }

    private List<Location> mapCreatingDataToLocations(String creatingData)
            throws InvalidLocationInitFileCreatingDataException {
        ObjectMapper mapper = new ObjectMapper();
        if (!isLocationDataValid(creatingData)) {
            throw new InvalidLocationInitFileCreatingDataException(
                    getExceptionMessageSource("exception.invalid.locations.file.creating.data"));
        }
        return Arrays.stream(creatingData.split("},"))
                .map(s -> s.replaceAll("[\\[\\]]", ""))
                .map(s -> s.replace("{\"en\":", "{\"lang\":\"EN\""))
                .map(s -> s.replace("\"ua\":", "{\"lang\":\"UA\""))
                .map(s -> s.replace("\"ru\":", "{\"lang\":\"RU\""))
                .map(s -> s + "}")
                .map(s -> s.replace("}}", "}"))
                .map(s -> s.replace("{\"city\"", ",\"city\""))
                .map(s -> {
                    try {
                        return mapper.readValue(s, Location.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
