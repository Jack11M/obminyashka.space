package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.exception.InvalidLocationInitFileCreatingDataException;
import com.hillel.items_exchange.model.Location;

import java.io.IOException;
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
     * @return the location with the given id or {@link Optional#empty()} if none found.
     */
    Optional<Location> findById(long id);

    /**
     * Retrieves locations by its id.
     * @param ids List of Location IDs.
     * @return the Location DTOs with the given ids.
     */
    List<LocationDto> findByIds(List<Long> ids);

    /**
     * Returns an {@code Optional} describing a {@link LocationDto} by its id, if
     * non-{@code null}, otherwise returns an empty {@code Optional}.
     * @param id Location ID.
     * @return the Location DTO with the given id or {@link Optional#empty()} if none found.
     */
    Optional<LocationDto> getById(long id);

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

    /**
     * Creates file to initialize locations in database.
     * @param creatingData must match the regular expression for creating of initialization file.
     * @return content of newly created file.
     */
    String createFileToInitLocations(String creatingData) throws IOException, InvalidLocationInitFileCreatingDataException;

    /**
     * Checks a parameter against a pattern of creating of initialization file.
     * @param creatingData must match the regular expression for creating of initialization file.
     * @return true if parameter matches the regular expression, false if it's not.
     */
    boolean isLocationDataValid(String creatingData);
}
