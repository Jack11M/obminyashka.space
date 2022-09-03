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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

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
        String initString = "INSERT INTO location (id, city_ua, district_ua, area_ua, city_en, district_en, area_en) VALUES";
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(locationInitFilePath), StandardCharsets.UTF_8)) {
            writer.append(initString);
            for (int i = 0; i < locations.size(); i++) {
                Location location = locations.get(i);
                writer.append(String.format(" (UUID_TO_BIN('%s'),'%s','%s','%s','%s','%s','%s')",
                        location.getId(),
                        location.getCityUA().replace("'", "\\'"),
                        location.getDistrictUA().replace("'", "\\'"),
                        location.getAreaUA().replace("'", "\\'"),
                        location.getCityEN().replace("'", "\\'"),
                        location.getDistrictEN().replace("'", "\\'"),
                        location.getAreaEN().replace("'", "\\'")));
                if (i < locations.size() - 1) writer.append(",");
            }
        }
        return Files.readString(Path.of(locationInitFilePath).toAbsolutePath(), StandardCharsets.UTF_8);
    }

    private List<Location> mapCreatingDataToLocations(List<RawLocation> creatingData) {
        return creatingData.stream()
                .filter(Predicate.not(rawLocation -> rawLocation.getFullAddressEn().isBlank()))
                .map(this::parseRawLocation)
                .distinct()
                .toList();
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

        if (enSplit[1].contains("area") || enSplit[1].contains("region")) {
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
     * @param unparsedLocation string with a prefix object type and its name. Example: 'region Kyivska'
     * @return capital part of the string without prefix. Example: 'Kyivska'
     */
    private String extractLocationPart(String unparsedLocation) {
        final var pgtPrefixLength = "PGT".length();
        return unparsedLocation.chars().skip(pgtPrefixLength)
                .filter(Character::isUpperCase)
                .findFirst()
                .stream()
                .mapToObj(firstCapitalLetter -> unparsedLocation.substring(unparsedLocation.indexOf(firstCapitalLetter, pgtPrefixLength)))
                .findFirst()
                .orElse("");
    }
}
