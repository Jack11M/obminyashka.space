package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    /**
     * Returns all Location DTOs.
     * @return list of location entities from DB that are represented as {@link LocationDto}
     */
    List<LocationDto> findAll();

    /**
     * Retrieves a location by its id.
     * @param id Location ID.
     * @return the location with the given id or Optional#empty() if none found.
     */
    Optional<Location> findById(long id);

    /**
     * If a location exists, returns the Location DTO, otherwise throws {@code ClassNotFoundException}.
     * @param id Location ID.
     * @return the non-{@code null} {@link LocationDto} with given id.
     * @throws ClassNotFoundException if no location found.
     */
    LocationDto getById(long id) throws ClassNotFoundException;

    /**
     * Creates a new record in table location in DB.
     * @param location must not be null.
     * @return the created Location DTO; will never be null.
     */
    Location save(Location location);

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
    void removeById(long id);

    /**
     * Removes the locations with the given ids from DB.
     * @param ids list of Location IDs to remove.
     */
    void removeById(List<Long> ids);

    /**
     * If a location exists, returns {@code true}, otherwise {@code false}.
     * @param id Location ID.
     * @return true if a location with the given id exists, false otherwise.
     */
    boolean existsById(long id);

    /**
     * Updates a record in location table.
     * @param locationDto must not be null.
     * @return the updated Location DTO; will never be null.
     */
    LocationDto update(LocationDto locationDto);
}