package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.exception.UnsupportedMediaTypeException;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    /**
     * Return byte representative of images for received Advertisement ID
     * @param advertisementId Advertisement ID
     * @return all images that are linked to the Advertisement
     */
    List<byte[]> getImagesResourceByAdvertisementId(long advertisementId);

    /**
     * Return all Image DTO for received Advertisement ID
     * @param advertisementId Advertisement ID
     * @return list of ImageDTO that are linked
     */
    List<ImageDto> getByAdvertisementId(long advertisementId);

    /**
     * Make in-memory compressing (20% of basic quality) only for supported types of images
     * @param images list of images for further compression
     * @return compressed images' bytes
     * @throws IOException in cases when some of received images is corrupted or it's impossible to read it properly
     * @throws UnsupportedMediaTypeException in case receiving unsupported types
     * @see SupportedMediaTypes supported media types
     */
    List<byte[]> compress(List<MultipartFile> images) throws IOException, UnsupportedMediaTypeException;

    /**
     * Make in-memory compressing (30% of basic quality) only for supported types of images
     * @param image image for further compression
     * @return compresses image's bytes
     * @throws IOException in cases when received image is corrupted or it's impossible to read it properly
     * @throws UnsupportedMediaTypeException in case receiving unsupported types
     * @see SupportedMediaTypes supported media types
     */
    byte[] compress(MultipartFile image) throws IOException, UnsupportedMediaTypeException;

    /**
     * Create new entity for each received image, link them to the Advertisement using it's ID and store them to the DB
     * @see Image entity as representation of all images
     * @param advertisement to link images with
     * @param images list of images that need to be linked with the Advertisement and saved to the DB
     */
    void saveToAdvertisement(Advertisement advertisement, List<byte[]> images);

    /**
     * Create new entity for received image, link to the Advertisement using it's ID and store it to the DB
     * @param advertisement advertisement to link images with
     * @param image image that needs to be linked with the Advertisement and saved to the DB
     */
    void saveToAdvertisement(Advertisement advertisement, byte[] image);

    /**
     * Check if the image exists into the DB by its ID
     * @param imageId ID of the image to check
     * @return result of check
     */
    boolean isExistsById(long imageId);

    /**
     * Check whether all images with gained IDs received into an advertisement
     * @param ids images IDs to check
     * @param advertisementId ID of the advertisement
     * @return {@literal true} if all image IDs exist into the advertisement
     */
    boolean existAllById(List<Long> ids, long advertisementId);

    /**
     * Remove received images from the DB using their ID
     * @param imageIdList ID images for removing
     */
    void removeById(List<Long> imageIdList);

    /**
     * Remove image from the DB by it's ID
     * @param imageId ID image to remove
     */
    void removeById(long imageId);

    /**
     * Makes scaled image (thumbnail) from given image bytes
     * @param bytes bytes representative of image to scale
     * @return scaled image bytes
     */
    byte[] scale(byte[] bytes);
}
