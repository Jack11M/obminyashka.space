package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.LocationRepository;
import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.dto.RawLocation;
import space.obminyashka.items_exchange.model.Location;
import space.obminyashka.items_exchange.service.LocationService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static space.obminyashka.items_exchange.mapper.UtilMapper.convertAllTo;
import static space.obminyashka.items_exchange.mapper.UtilMapper.convertTo;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    @Value("${location.init.file.path}")
    private String locationInitFilePath;

    @Override
    public List<LocationDto> findAll() {
        return convertAllTo(locationRepository.findAll(), LocationDto.class);
    }

    @Override
    public List<LocationDto> findAllForCurrentLanguage(Locale lang) {
        return convertAllTo(locationRepository.findByI18nIgnoreCase(lang.getLanguage()), LocationDto.class);
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<LocationDto> findByIds(List<UUID> ids) {
        List<Location> locations = locationRepository.findByIdIn(ids);
        return convertAllTo(locations, LocationDto.class);
    }

    @Override
    public Optional<LocationDto> getById(UUID id) {
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
    public void removeById(UUID id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void removeById(List<UUID> ids) {
        ids.forEach(this::removeById);
    }

    @Override
    public boolean existsById(UUID id) {
        return locationRepository.existsById(id);
    }

    @Override
    public LocationDto update(LocationDto locationDto) {
        Location location = convertTo(locationDto, Location.class);
        Location updatedLocation = locationRepository.saveAndFlush(location);
        return convertTo(updatedLocation, LocationDto.class);
    }

    @Override
    public String createParsedLocationsFile(List<RawLocation> creatingData) throws IOException {

        List<Location> locations = mapCreatingDataToLocations(creatingData);
        String initString = "INSERT INTO location (id, city, district, area, i18n) VALUES";
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(locationInitFilePath), StandardCharsets.UTF_8)) {
            writer.append(initString);
            for (int i = 0; i < locations.size(); i++) {
                Location location = locations.get(i);
                writer.append(String.format(" (UUID_TO_BIN('%s'),'%s','%s','%s','%s')",
                        UUID.randomUUID(),
                        location.getCity().replace("'", "\\'"),
                        location.getDistrict().replace("'", "\\'"),
                        location.getArea().replace("'", "\\'"),
                        location.getI18n()));
                if (i < locations.size() - 1) writer.append(",");
            }
        }
        return Files.readString(Path.of(locationInitFilePath).toAbsolutePath(), StandardCharsets.UTF_8);
    }

    private List<Location> mapCreatingDataToLocations(List<RawLocation> creatingData) {
        return creatingData.stream()
                .flatMap(rawLocation -> {
                    final Location en = setLocaleAndReturn(rawLocation.getEn(), "en");
                    final Location ua = setLocaleAndReturn(rawLocation.getUa(), "ua");
                    final Location ru = setLocaleAndReturn(rawLocation.getRu(), "ru");
                    return Stream.of(en, ua, ru);
                }).toList();
    }

    private static Location setLocaleAndReturn(Location location, String en) {
        location.setI18n(new Locale(en).getLanguage());
        return location;
    }
}
