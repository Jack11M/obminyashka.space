package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dto.LocationDto;
import space.obminyashka.items_exchange.dto.RawLocation;
import space.obminyashka.items_exchange.model.Location;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationService {
    /**
     * Returns all Location DTOs.
     * @return list of location entities from DB that are represented as {@link LocationDto}
     */
    List<LocationDto> findAll();

    /**
     * Retrieves a location by its id.
     * @param id Location ID.
     * @return the location with the given id or {@link Optional#empty()} if none found.
     */
    Optional<Location> findById(UUID id);

    /**
     * Retrieves locations by its id.
     * @param ids List of Location IDs.
     * @return the Location DTOs with the given ids.
     */
    List<LocationDto> findByIds(List<UUID> ids);

    /**
     * Returns an {@code Optional} describing a {@link LocationDto} by its id, if
     * non-{@code null}, otherwise returns an empty {@code Optional}.
     * @param id Location ID.
     * @return the Location DTO with the given id or {@link Optional#empty()} if none found.
     */
    Optional<LocationDto> getById(UUID id);

    /**
     * Creates a new record in table location in DB.
     * @param locationDto must not be null.
     * @return the created Location DTO; will never be null.
     */
    LocationDto save(LocationDto locationDto);

    /**
     * Removes the location with the given id from DB.
     * @param id Location ID to remove.
     */
    void removeById(UUID id);

    /**
     * Removes the locations with the given ids from DB.
     * @param ids list of Location IDs to remove.
     */
    void removeById(List<UUID> ids);

    /**
     * If a location exists, returns {@code true}, otherwise {@code false}.
     * @param id Location ID.
     * @return true if a location with the given id exists, false otherwise.
     */
    boolean existsById(UUID id);

    /**
     * Updates a record in location table.
     * @param locationDto must not be null.
     * @return the updated Location DTO; will never be null.
     */
    LocationDto update(LocationDto locationDto);

    /**
     * Creates file to initialize locations in database.
     * @param creatingData must match the regular expression for creating of initialization file.
     * @return content of newly created file.
     * @throws IOException when there is error of writing data to newly created locations file.
     */
    String createParsedLocationsFile(List<RawLocation> creatingData) throws IOException;
}
