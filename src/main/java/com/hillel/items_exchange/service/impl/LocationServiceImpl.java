package com.hillel.items_exchange.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.items_exchange.dao.LocationRepository;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.exception.InvalidLocationInitFileCreatingDataException;
import com.hillel.items_exchange.model.Location;
import com.hillel.items_exchange.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.mapper.UtilMapper.convertAllTo;
import static com.hillel.items_exchange.mapper.UtilMapper.convertTo;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private static final String LANG_PATTERN = "\"[a-z]{2}\"";
    private static final String LETTERS_PATTERN = "\"[ A-Za-zА-Яа-яЁёЇїІіЄєҐґ'’-]{0,30}\"";
    public static final String LOCATION_INIT_FILE_CREATE_DATA_PATTERN = LANG_PATTERN +
            ":\"city\":" + LETTERS_PATTERN +
            ",\"district\":" + LETTERS_PATTERN +
            ",\"area\":" + LETTERS_PATTERN;

    private final LocationRepository locationRepository;
    private final ObjectMapper mapper;
    private List<String> locationStings;
    @Value("${location.init.file.path}")
    private String locationInitFilePath;

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
    public String createParsedLocationsFile(String creatingData)
            throws IOException, InvalidLocationInitFileCreatingDataException {
        File file = new File(locationInitFilePath);
        List<Location> locations = mapCreatingDataToLocations(creatingData);
        String initString = "INSERT INTO `evo_exchange`.`location` (`id`, `city`, `district`, `area`, `i18n`) VALUES";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.append(initString);
            for (int i = 0; i < locations.size(); i++) {
                Location location = locations.get(i);
                writer.append(String.format(" ('%s','%s','%s','%s','%s')",
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
        locationStings = new ArrayList<>();
        return Arrays.stream(locationDataString.substring(1, locationDataString.length() - 1).split("},"))
                .map(s -> s.replaceAll("\\{|\\}", ""))
                .peek(locationStings::add)
                .allMatch(s -> pattern.matcher(s).matches());
    }

    private List<Location> mapCreatingDataToLocations(String creatingData)
            throws InvalidLocationInitFileCreatingDataException {
        if (!isLocationDataValid(creatingData)) throw new InvalidLocationInitFileCreatingDataException(
                getExceptionMessageSource("exception.invalid.locations.file.creating.data"));
        return locationStings
                .stream()
                .map(this::prepareLocation)
                .map(this::parseLocation)
                .collect(Collectors.toList());
    }

    private String prepareLocation(String locationLine) {
        Matcher matcher = Pattern.compile(LANG_PATTERN).matcher(locationLine);
        if (matcher.find()) {
            String i18nString = locationLine.substring(matcher.start(), matcher.end());
            String replacePattern = "{\"lang\":%s,";
            return locationLine.replaceAll(LANG_PATTERN + ":", String.format(replacePattern, i18nString.toUpperCase())) + "}";
        }
        return locationLine;
    }

    @SneakyThrows
    private Location parseLocation(String location) {
        return mapper.readValue(location, Location.class);
    }
}
