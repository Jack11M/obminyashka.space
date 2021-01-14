package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.AdvertisementTitleDto;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.User;
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
    List<AdvertisementDto> findFirst10ByTopic(String topic);

    /**
     * Find an advertisement by id
     * @param advertisementId advertisement id
     * @return {@link Optional} as result
     */
    Optional<Advertisement> findById(long advertisementId);

    /**
     * Find an advertisement DTO by id
     * @param id advertisement id
     * @return {@link Optional} as result
     */
    Optional<AdvertisementDto> findDtoById(long id);

    /**
     * Find first 10 matched advertisements by one of received parameters of the request DTO
     * @param dto an object that contains all parameters to search
     * @return result of the request
     */
    List<AdvertisementDto> findFirst10AdvertisementsByMultipleParams(AdvertisementFilterDto dto);

    /**
     * Check whenever user has an advertisement with selected id
     * @param id advertisement id
     * @param user for checking authority
     * @return result of the check
     */
    boolean isUserHasAdvertisementWithId(long id, User user);

    /**
     * Create a new advertisement
     * @param advertisementDto DTO for converting and saving
     * @param user owner of a new advertisement
     * @return saved advertisement DTO with updated id
     */
    AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto, User user);

    /**
     * Update existed advertisement
     * @param dto advertisement DTO for converting and updating
     * @return updated advertisement DTO
     */
    AdvertisementDto updateAdvertisement(AdvertisementDto dto);

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
}
