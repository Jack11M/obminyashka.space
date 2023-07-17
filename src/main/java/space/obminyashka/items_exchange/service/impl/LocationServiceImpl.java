package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.repository.LocationRepository;
import space.obminyashka.items_exchange.rest.dto.LocationDto;
import space.obminyashka.items_exchange.rest.request.RawLocation;
import space.obminyashka.items_exchange.rest.mapper.LocationMapper;
import space.obminyashka.items_exchange.repository.model.Location;
import space.obminyashka.items_exchange.service.LocationService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private static final Set<String> INVALID_LOCATIONS_UA = Set.of(
            "Україна,область Київська,місто Ірпінь,Селище міського типу Ворзель",
            "Україна,область Київська,місто Буча");
    private static final Set<String> INVALID_LOCATIONS_EN = Set.of(
            "Ukraine,area Odeska,district Ovidiopolskyi,village Limanka",
            "Ukraine,area Zakarpatska,district Mukachivskyi,city Mukacheve"
    );
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    @Value("${location.init.file.path}")
    private String locationInitFilePath;


    @Override
    public List<LocationDto> findAll() {
        return locationMapper.toDtoList(locationRepository.findAll());
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

        List<Location> locations = mapCreatingDataToLocations(creatingData);
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

    private List<Location> mapCreatingDataToLocations(List<RawLocation> creatingData) {
        return creatingData.stream()
                .filter(Predicate.not(LocationServiceImpl::isLocationInvalid))
                .map(this::parseRawLocation)
                .distinct()
                .toList();
    }

    private static boolean isLocationInvalid(RawLocation rawLocation) {
        if (rawLocation.getFullAddressEn().isBlank()) {
            return true;
        }
        final var isInvalidEnglish = INVALID_LOCATIONS_EN.stream().anyMatch(it -> rawLocation.getFullAddressEn().startsWith(it));
        final var isInvalidUkrainian = INVALID_LOCATIONS_UA.stream().anyMatch(it -> rawLocation.getFullAddressUa().startsWith(it));
        return isInvalidEnglish || isInvalidUkrainian;
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
     *
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
                .map(this::decoratePossibleApostropheChars)
                .findFirst()
                .orElse("");
    }

    private String decoratePossibleApostropheChars(String stringToDecorate) {
        return stringToDecorate.replace("’", "'").replace("'", "\\'");
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
