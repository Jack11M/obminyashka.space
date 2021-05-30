package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdvertisementService {

    /**
     * Find all advertisements and return them by requested quantity (size) and page
     * @param pageable see {@link Pageable} for more details
     * @return wanted quantity of advertisement on a page
     */
    List<AdvertisementDto> findAll(Pageable pageable);

    /**
     * Find all advertisements as thumbnails and return them by requested quantity (size) and page
     * @param pageable see {@link Pageable} for more details
     * @return wanted quantity of advertisement on a page
     */
    List<AdvertisementTitleDto> findAllThumbnails(Pageable pageable);

    /**
     * Find first 10 advertisements having requested topic
     * @param topic - searched topic
     * @return result of the request
     */
    List<AdvertisementTitleDto> findFirst10ByTopic(String topic);

    /**
     * Find an advertisement by id
     * @param advertisementId advertisement id
     * @return {@link Optional} as result
     */
    Optional<Advertisement> findById(long advertisementId);

    /**
     * Find an advertisement with additional owner check
     * @param advertisementId ID of an advertisement
     * @param ownerName login or email of the advertisement's owner
     * @return {@link Optional} as result
     */
    Optional<Advertisement> findByIdAndOwnerUsername(long advertisementId, String ownerName);

    /**
     * Find an advertisement Display DTO by id
     * @param id advertisement id
     * @return {@link Optional} as result
     */
    Optional<AdvertisementDisplayDto> findDtoById(long id);

    /**
     * Find first 10 matched advertisements by one of received parameters of the request DTO
     * @param dto an object that contains all parameters to search
     * @return result of the request
     */
    List<AdvertisementTitleDto> findFirst10ByFilter(AdvertisementFilterDto dto);

    /**
     * Check whenever user has an advertisement with selected id
     * @param id advertisement id
     * @param user for checking authority
     * @return result of the check
     */
    boolean isUserHasAdvertisementWithId(long id, User user);

    /**
     * Create a new advertisement
     * @param advertisementModificationDto DTO for converting and saving
     * @param user owner of a new advertisement
     * @return saved advertisement DTO with updated id
     */
    AdvertisementModificationDto createAdvertisement(AdvertisementModificationDto advertisementModificationDto, User user);

    /**
     * Update existed advertisement
     * @param dto advertisement DTO for converting and updating
     * @return updated advertisement DTO
     */
    AdvertisementModificationDto updateAdvertisement(AdvertisementModificationDto dto);

    /**
     * Remove an advertisement by it's id
     * @param id id of the advertisement to remove
     */
    void remove(long id);

    /**
     * Set an image as title image of an advertisement
     * @param advertisement advertisement for setting the title image
     * @param imageId id of an image that planned to be set as title image
     * @param owner user-author of selected advertisement
     */
    void setDefaultImage(Advertisement advertisement, Long imageId, User owner);

    /**
     * Check if a user owns such advertisement and it has selected image
     * @param advertisementId id of selected advertisement to check
     * @param imageId id of an image to check into the advertisement
     * @param owner user to check the advertisement and the image
     * @return result of the check
     */
    boolean isUserHasAdvertisementAndItHasImageWithId(Long advertisementId, Long imageId, User owner);

    /**
     * Returns whether an advertisement with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an advertisement with the given id exists, {@literal false} otherwise.
     */
    boolean existById(Long id);
}
