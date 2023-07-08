package space.obminyashka.items_exchange.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import space.obminyashka.items_exchange.controller.request.AdvertisementFilterRequest;
import space.obminyashka.items_exchange.dto.AdvertisementDisplayDto;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.dto.AdvertisementTitleDto;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdvertisementService {

    /**
     * Find all advertisements as thumbnails for specific user
     * @param username login of the user
     * @return all advertisements created by the user
     */
    List<AdvertisementTitleDto> findAllByUsername(String username);

    /**
     * Find all favorites advertisement by username
     *
     * @param username login of the user
     * @return all favorites advertisement by username
     */
    Page<AdvertisementTitleDto> findAllFavorite(String username, Pageable pageable);

    /**
     * Add favorite advertisement by username and advertisementId
     *
     * @param advertisementId id of existence advertisement
     * @param username login of the user
     */
    List<Advertisement> addFavorite(UUID advertisementId, String username);

    /**
     * Save favorite advertisement by username
     *
     * @param list list of favorite advertisement
     * @param username login of the user
     */
    void saveFavorite(List<Advertisement> list, String username);

    /**
     * Delete favorite advertisement by username and advertisementId
     *
     * @param advertisementId id of existence advertisement
     * @param username login of the user
     */
    void deleteFavorite(UUID advertisementId, String username);

    /**
     * Find advertisements having requested keyword
     * @param keyword - searched word
     * @param pageable see {@link Pageable} for more details
     * @return result of the request
     */
    Page<AdvertisementTitleDto> findByKeyword(String keyword, Pageable pageable);

    /**
     * Find an advertisement with additional owner check
     * @param advertisementId ID of an advertisement
     * @param ownerName login or email of the advertisement's owner
     * @return {@link Optional} as result
     */
    Optional<Advertisement> findByIdAndOwnerUsername(UUID advertisementId, String ownerName);

    /**
     * Find an advertisement Display DTO by id
     * @param id advertisement id
     * @return {@link Optional} as result
     */
    Optional<AdvertisementDisplayDto> findDtoById(UUID id);

    /**
     * Filter advertisements by search parameters from AdvertisementFilterRequest
     * @param request an object that contains all parameters to search
     * @return result of the request
     */
    Page<AdvertisementTitleDto> filterAdvertisementBySearchParameters(AdvertisementFilterRequest request);

    /**
     * Check whenever user has an advertisement with selected id
     * @param id advertisement id
     * @param user for checking authority
     * @return result of the check
     */
    boolean isUserHasAdvertisementWithId(UUID id, User user);

    /**
     * Create a new advertisement
     * @param modificationDto DTO for converting and saving
     * @param user owner of a new advertisement
     * @param compressedImages images related to an advertisement
     * @param titleImage scaled title image
     * @return saved advertisement DTO with updated id
     */
    AdvertisementModificationDto createAdvertisement(AdvertisementModificationDto modificationDto, User user,
                                                     List<byte[]> compressedImages, byte[] titleImage);

    /**
     * Update existed advertisement
     * @param dto advertisement DTO for converting and updating
     * @return updated advertisement DTO
     */
    AdvertisementModificationDto updateAdvertisement(AdvertisementModificationDto dto);

    /**
     * Remove an advertisement by its id
     * @param id id of the advertisement to remove
     */
    void remove(UUID id);

    /**
     * Set an image as title image of an advertisement
     * @param advertisement advertisement for setting the title image
     * @param imageId id of an image that planned to be set as title image
     */
    void setDefaultImage(Advertisement advertisement, UUID imageId);

    /**
     * Check if a user owns such advertisement, and it has selected image
     * @param advertisementId id of selected advertisement to check
     * @param imageId id of an image to check into the advertisement
     * @param owner user to check the advertisement and the image
     * @return result of the check
     */
    boolean isUserHasAdvertisementAndItHasImageWithId(UUID advertisementId, UUID imageId, User owner);

    /**
     * Returns whether an advertisement with the given id exists.
     * @param id must not be {@literal null}.
     * @return {@literal true} if an advertisement with the given id exists, {@literal false} otherwise.
     */
    boolean existById(UUID id);

    /**
     * Count total amount of existed advertisements
     * @return quantity of saved advertisements
     */
    long count();

    /**
     * Checks if a subcategory with the given ID exists in DB and has advertisements.
     * @param id is Subcategory ID.
     * @return {@code true} if a subcategory with the given ID can not be deleted, {@code false} otherwise.
     */
    boolean areAdvertisementsExistWithSubcategory(long id);
}
