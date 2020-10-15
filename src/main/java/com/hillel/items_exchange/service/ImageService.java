package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.exception.UnsupportedMediaTypeException;
import com.hillel.items_exchange.model.Image;
import com.hillel.items_exchange.model.Product;
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
     * Create new entity for each received image, link them to the Product using it's ID and store them to the DB
     * @see Image entity as representation of all images
     * @param product to link images with
     * @param images list of images that need to be linked with the Product and saved to the DB
     */
    void saveToProduct(Product product, List<byte[]> images);

    /**
     * Create new entity for received image, link to the Product using it's ID and store it to the DB
     * @param product to link images with
     * @param image image that needs to be linked with the Product and saved to the DB
     */
    void saveToProduct(Product product, byte[] image);

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
     * @throws IOException in cases when received bytes impossible to read it properly
     */
    byte[] scale(byte[] bytes) throws IOException;
}
