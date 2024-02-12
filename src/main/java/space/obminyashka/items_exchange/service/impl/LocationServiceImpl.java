package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.repository.AreaRepository;
import space.obminyashka.items_exchange.repository.CityRepository;
import space.obminyashka.items_exchange.repository.DistrictRepository;
import space.obminyashka.items_exchange.repository.LocationRepository;
import space.obminyashka.items_exchange.repository.model.Area;
import space.obminyashka.items_exchange.repository.model.City;
import space.obminyashka.items_exchange.repository.model.District;
import space.obminyashka.items_exchange.repository.model.Location;
import space.obminyashka.items_exchange.repository.model.base.BaseLocation;
import space.obminyashka.items_exchange.rest.dto.LocationDto;
import space.obminyashka.items_exchange.rest.mapper.LocationMapper;
import space.obminyashka.items_exchange.rest.request.RawLocation;
import space.obminyashka.items_exchange.rest.response.LocationNameView;
import space.obminyashka.items_exchange.service.LocationService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private static final Map<Class<? extends BaseLocation>, String> LOCATION_STRING_MAP = Map.of(
            Area.class, "INSERT INTO area (id, name_en, name_ua) VALUES ",
            District.class, "INSERT INTO district (id, area_id, name_en, name_ua) VALUES ",
            City.class, "INSERT INTO city (id, district_id, name_en, name_ua) VALUES "
    );

    private final LocationRepository locationRepository;
    private final AreaRepository areaRepository;
    private final DistrictRepository districtRepository;

    private final CityRepository cityRepository;

    private final LocationMapper locationMapper;

    @Value("${location.init.file.path}")
    private String locationInitFilePath;

    @Override
    public List<LocationDto> findAll() {
        return locationMapper.toDtoList(locationRepository.findAll());
    }

    @Override
    public List<LocationNameView> findAllAreas() {
        return locationMapper.toNameViewList(areaRepository.findAll());
    }

    @Override
    public List<LocationNameView> findAllDistrictsByAreaId(UUID areaId) {
        return locationMapper.toDistrictNameViewList(districtRepository.findAllByAreaId(areaId));
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<LocationDto> findByIds(List<UUID> ids) {
        List<Location> locations = locationRepository.findByIdIn(ids);
        return locationMapper.toDtoList(locations);
    }

    @Override
    public Optional<LocationDto> getById(UUID id) {
        return findById(id)
                .map(locationMapper::toDto);
    }

    @Override
    public LocationDto save(LocationDto locationDto) {
        Location location = locationMapper.toModel(locationDto);
        Location savedLocation = locationRepository.saveAndFlush(location);
        return locationMapper.toDto(savedLocation);
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
        Location location = locationMapper.toModel(locationDto);
        Location updatedLocation = locationRepository.saveAndFlush(location);
        return locationMapper.toDto(updatedLocation);
    }

    @Override
    public String createParsedLocationsFile(List<RawLocation> creatingData) throws IOException {
        Map<Area, Area> areas = new HashMap<>();
        Map<District, District> districts = new HashMap<>();
        Map<City, City> cities = new HashMap<>();

        for (RawLocation location : creatingData) {
            Area area = areas.computeIfAbsent(new Area(location), Function.identity());

            District district = districts.computeIfAbsent(new District(location, area), Function.identity());

            cities.computeIfAbsent(new City(location, district), Function.identity());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(locationInitFilePath), StandardCharsets.UTF_8)) {
            writeLocationsToWriter(writer, new HashSet<>(areas.values()), Area.class);
            writeLocationsToWriter(writer, new HashSet<>(districts.values()), District.class);
            writeLocationsToWriter(writer, new HashSet<>(cities.values()), City.class);
        }

        return String.valueOf(areas.size() + districts.size() + cities.size());
    }

    private <T extends BaseLocation> void writeLocationsToWriter(BufferedWriter writer, Set<T> locations, Class<T> locationClass) throws IOException {
        StringJoiner stringJoiner = new StringJoiner(",", LOCATION_STRING_MAP.get(locationClass), "");
        locations.forEach(location -> stringJoiner.add(location.formatForSQL()));
        writer.append(stringJoiner.toString());
        writer.append(";");
    }

    @Override
    public List<LocationNameView> getAllCityByDistrictId(UUID id) {
        return locationMapper.toCityNameViewList(cityRepository.findAllByDistrictId(id));
    }

    @Override
    public boolean existDistricts(UUID id) {
        return districtRepository.existsById(id);
    }

}
