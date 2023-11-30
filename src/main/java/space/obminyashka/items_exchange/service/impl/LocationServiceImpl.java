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
import space.obminyashka.items_exchange.rest.request.LocationRaw;
import space.obminyashka.items_exchange.rest.request.RawLocation;
import space.obminyashka.items_exchange.rest.response.LocationNameView;
import space.obminyashka.items_exchange.service.LocationService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    @Value("${locs.init.file.path}")
    private String locsInitFilePath;

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
        List<Location> locations = creatingData.stream()
                .map(this::parseRawLocation)
                .distinct()
                .toList();

        String initString = "INSERT INTO location (id, city_ua, district_ua, area_ua, city_en, district_en, area_en) VALUES";
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(locationInitFilePath), StandardCharsets.UTF_8)) {
            writer.append(initString);
            for (int i = 0; i < locations.size(); i++) {
                Location location = locations.get(i);
                writer.append(locationToFormatterString(location));
                if (i < locations.size() - 1) writer.append(",");
            }
        }
        return Files.readString(Path.of(locationInitFilePath).toAbsolutePath(), StandardCharsets.UTF_8);
    }

    @Override
    public String createParsedLocsFile(List<LocationRaw> creatingData) throws IOException {
        Map<String, Area> areas = new HashMap<>();
        Map<String, District> districts = new HashMap<>();
        Map<String, City> cities = new HashMap<>();

        for (LocationRaw location : creatingData) {
            if (!location.getAreaUa().isBlank() && !location.getAreaEn().isBlank()) {
                Area area = areas.computeIfAbsent(location.getAreaEn(), key -> new Area(location));

                if (!location.getDistrictUa().isBlank() && !location.getDistrictEn().isBlank()) {
                    District district = districts.computeIfAbsent(location.getDistrictEn(), key -> new District(location, area));

                    if (!location.getCityUa().isBlank() && !location.getCityEn().isBlank()) {
                        cities.computeIfAbsent(location.getCityEn(), key -> new City(location, district));
                    }
                }
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(locsInitFilePath), StandardCharsets.UTF_8)) {
            writeLocationsToWriter(writer, new HashSet<>(areas.values()), Area.class);
            writeLocationsToWriter(writer, new HashSet<>(districts.values()), District.class);
            writeLocationsToWriter(writer, new HashSet<>(cities.values()), City.class);
        }

        return String.valueOf(areas.values().size() + districts.values().size() + cities.values().size());
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

    /**
     * The method extracts names of Area, District and City from three possible ways of full address incoming data
     * and populate a new Location object with it
     * <p>
     * Incoming data possible examples:
     * <p><h3>Full data (Area, District, City):</h3> Ukraine,region Luhanska,district Starobilskyi,city Starobilsk,street Tsentralna,building 1</p>
     * <p><h3>Partial data (Area, City):</h3> Ukraine,region Khersonska,city Kherson,street Potomkinska,building 2</p>
     * <p><h3>Minimal data (City):</h3> Ukraine,city Kyiv,street Danyla Shcherbakivskoho,building 3</p>
     * </p>
     *
     * @param rawLocation location in EN and UA locale full addresses
     * @return new Location populated with data
     */
    private Location parseRawLocation(RawLocation rawLocation) {
        var enSplit = rawLocation.getFullAddressEn().split(",");
        var uaSplit = rawLocation.getFullAddressUa().split(",");

        final var blankLocation = new Location(UUID.randomUUID(), "", "", "", "", "", "", null);
        int cityIndexCounter = 1;

        if (enSplit[1].contains("area")) {
            blankLocation.setAreaEN(extractLocationPart(enSplit[1]));
            blankLocation.setAreaUA(extractLocationPart(uaSplit[1]));
            cityIndexCounter++;

            if (enSplit[2].contains("district")) {
                blankLocation.setDistrictEN(extractLocationPart(enSplit[2]));
                blankLocation.setDistrictUA(extractLocationPart(uaSplit[2]));
                cityIndexCounter++;
            }
        }
        blankLocation.setCityEN(extractLocationPart(enSplit[cityIndexCounter]));
        blankLocation.setCityUA(extractLocationPart(uaSplit[cityIndexCounter]));
        return blankLocation;
    }

    /**
     * Extracts a string part that starts from capital letter
     *
     * @param unparsedLocation string with a prefix object type and its name. Example: 'region Kyivska'
     * @return capital part of the string without prefix. Example: 'Kyivska'
     */
    private String extractLocationPart(String unparsedLocation) {
        return unparsedLocation.chars()
                .filter(Character::isUpperCase)
                .findFirst()
                .stream()
                .mapToObj(firstCapitalLetter -> unparsedLocation.substring(unparsedLocation.indexOf(firstCapitalLetter)))
                .map(this::decoratePossibleApostropheChars)
                .findFirst()
                .orElse("");
    }

    private String decoratePossibleApostropheChars(String stringToDecorate) {
        return stringToDecorate.replace("'", "\\'");
    }

    private static String locationToFormatterString(Location location) {
        return String.format(" (UUID_TO_BIN('%s'),'%s','%s','%s','%s','%s','%s')",
                location.getId(),
                location.getCityUA(),
                location.getDistrictUA(),
                location.getAreaUA(),
                location.getCityEN(),
                location.getDistrictEN(),
                location.getAreaEN());
    }
}
