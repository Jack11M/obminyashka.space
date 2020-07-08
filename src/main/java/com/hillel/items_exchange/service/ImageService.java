package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    /**
     * Return byte representative of images for received Product ID
     * @param productId Product ID
     * @return all images that are linked to the Product
     */
    List<byte[]> getImagesResourceByProductId(long productId);

    /**
     * Return all Image DTO for received Product ID
     * @param productId Product ID
     * @return list of ImageDTO that are linked
     */
    List<ImageDto> getByProductId(long productId);

    /**
     * Make in-memory compressing (20% of basic quality) only for supported types of images
     * @param images list of images for further compression
     * @return compressed images' bytes
     * @throws IOException in cases when some of received images is corrupted or it's impossible to read it properly
     */
    List<byte[]> compressImages(List<MultipartFile> images) throws IOException;

    /**
     * Make in-memory compressing (20% of basic quality) only for supported types of images
     * @param image image for further compression
     * @return compresses image's bytes
     * @throws IOException in cases when received image is corrupted or it's impossible to read it properly
     */
    byte[] compressImage(MultipartFile image) throws IOException;

    /**
     * Create new entity for each received image, link them to the Product using it's ID and store them to the DB
     * @see Image entity as representation of all images
     * @param productId Product ID
     * @param images list of images that need to be linked with the Product and saved to the DB
     * @throws ClassNotFoundException in cases when Product with such ID is not exist in the DB
     */
    void saveToProduct(long productId, List<byte[]> images) throws ClassNotFoundException;

    /**
     * Create new entity for received image, link to the Product using it's ID and store it to the DB
     * @param productId Product ID
     * @param image image that needs to be linked with the Product and saved to the DB
     * @throws ClassNotFoundException
     */
    void saveToProduct(long productId, byte[] image) throws ClassNotFoundException;

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
}
