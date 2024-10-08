package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.repository.model.Location;
import space.obminyashka.items_exchange.rest.dto.LocationDto;
import space.obminyashka.items_exchange.rest.response.LocationNameView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationService {
    /**
     * Returns all Location DTOs.
     *
     * @return list of location entities from DB that are represented as {@link LocationDto}
     */
    List<LocationDto> findAll();

    /**
     * Returns all Location Name View of Area.
     *
     * @return list of areas name from DB that are represented as {@link LocationNameView}
     */
    List<LocationNameView> findAllAreas();

    /**
     * Retrieves a location by its id.
     *
     * @param id Location ID.
     * @return the location with the given id or {@link Optional#empty()} if none found.
     */
    Optional<Location> findById(UUID id);

    /**
     * Retrieves locations by its id.
     *
     * @param ids List of Location IDs.
     * @return the Location DTOs with the given ids.
     */
    List<LocationDto> findByIds(List<UUID> ids);

    /**
     * Returns an {@code Optional} describing a {@link LocationDto} by its id, if
     * non-{@code null}, otherwise returns an empty {@code Optional}.
     *
     * @param id Location ID.
     * @return the Location DTO with the given id or {@link Optional#empty()} if none found.
     */
    Optional<LocationDto> getById(UUID id);

    /**
     * Creates a new record in table location in DB.
     *
     * @param locationDto must not be null.
     * @return the created Location DTO; will never be null.
     */
    LocationDto save(LocationDto locationDto);

    /**
     * Removes the location with the given id from DB.
     *
     * @param id Location ID to remove.
     */
    void removeById(UUID id);

    /**
     * Removes the locations with the given ids from DB.
     *
     * @param ids list of Location IDs to remove.
     */
    void removeById(List<UUID> ids);

    /**
     * If a location exists, returns {@code true}, otherwise {@code false}.
     *
     * @param id Location ID.
     * @return true if a location with the given id exists, false otherwise.
     */
    boolean existsById(UUID id);

    /**
     * Updates a record in location table.
     *
     * @param locationDto must not be null.
     * @return the updated Location DTO; will never be null.
     */
    LocationDto update(LocationDto locationDto);

    List<LocationNameView> getAllCityByAreaId(UUID id);

    /**
     * checking the existence of an area by ID
     *
     * @param id area ID
     * @return true if area exist or false if not
     */
    boolean existAreaById(UUID id);
}
