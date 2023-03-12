package space.obminyashka.items_exchange.service;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.exception.UnsupportedMediaTypeException;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    /**
     * Return byte representative of images for received Advertisement ID
     * @param advertisementId Advertisement ID
     * @return all images that are linked to the Advertisement
     */
    List<byte[]> getImagesResourceByAdvertisementId(UUID advertisementId);

    /**
     * Return all Image DTO for received Advertisement ID
     * @param advertisementId Advertisement ID
     * @return list of ImageDTO that are linked
     */
    List<ImageDto> getByAdvertisementId(UUID advertisementId);

    /**
     * Make in-memory compressing (30% of basic quality) only for supported types of images
     * @param image image for further compression
     * @return compresses image's bytes
     * @apiNote throw {@link IOException} in cases when received image is corrupted, or it's impossible to read it properly AND
     * throw {@link UnsupportedMediaTypeException} in case receiving unsupported types
     * @see SupportedMediaTypes supported media types
     */
    @SneakyThrows({IOException.class, UnsupportedMediaTypeException.class})
    byte[] compress(MultipartFile image);

    /**
     * Create new entity for each received image, link them to the Advertisement using its ID and store them to the DB
     * @see Image entity as representation of all images
     * @param advertisement to link images with
     * @param images list of images that need to be linked with the Advertisement and saved to the DB
     */
    void saveToAdvertisement(Advertisement advertisement, List<byte[]> images);

    /**
     * Create new entity for received image, link to the Advertisement using its ID and store it to the DB
     * @param advertisement advertisement to link images with
     * @param image image that needs to be linked with the Advertisement and saved to the DB
     */
    void saveToAdvertisement(Advertisement advertisement, byte[] image);

    /**
     * Check whether all images with gained IDs received into an advertisement
     * @param ids images IDs to check
     * @param advertisementId ID of the advertisement
     * @return {@literal true} if all image IDs exist into the advertisement
     */
    boolean existAllById(List<UUID> ids, UUID advertisementId);

    /**
     * Remove received images from the DB using their ID
     * @param imageIdList ID images for removing
     */
    void removeById(List<UUID> imageIdList);

    /**
     * Remove image from the DB by its ID
     * @param imageId ID image to remove
     */
    void removeById(UUID imageId);

    /**
     * Makes scaled image (thumbnail) from given image
     * @param image received representative of image to scale
     * @return scaled image bytes
     */
    byte[] scale(MultipartFile image);

    /**
     * Makes scaled image (thumbnail) from given image bytes
     * @param bytes bytes representative of image to scale
     * @return scaled image bytes
     */
    byte[] scale(byte[] bytes);

    /**
     * Making count of all images into an advertisement by its ID
     * @param id advertisement ID
     * @return total quantity of images already stored for selected advertisement
     */
    int countImagesForAdvertisement(UUID id);
}
