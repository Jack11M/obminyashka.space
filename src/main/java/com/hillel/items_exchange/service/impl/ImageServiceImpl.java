package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.ImageRepository;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.Image;
import com.hillel.items_exchange.model.Product;
import com.hillel.items_exchange.service.ImageService;
import com.hillel.items_exchange.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public List<byte[]> getImagesResourceByProductId(long productId) {
        return imageRepository.findByProductId(productId).stream()
                .map(Image::getResource)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDto> getByProductId(long productId) {
        return mapImagesToDto(imageRepository.findByProductId(productId));
    }

    private List<ImageDto> mapImagesToDto(Iterable<Image> images) {
        return modelMapper.map(images, new TypeToken<List<ImageDto>>() {}.getType());
    }

    @Override
    public List<byte[]> compressImages(List<MultipartFile> images) throws IOException {
        List<byte[]> compressedImages = new ArrayList<>();
        for (MultipartFile photo : images) {
            compressedImages.add(compressImage(photo));
        }
        return compressedImages;
    }

    @Override
    public byte[] compressImage(MultipartFile image) throws IOException {

        String contentType = image.getContentType();
        if (contentType == null || contentType.equals(MediaType.IMAGE_GIF_VALUE)) {
            return image.getBytes();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(baos);
             ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {

            ImageWriter writer = ImageIO.getImageWritersByMIMEType(contentType).next();
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                float quality = 0.20f; //percents
                param.setCompressionQuality(quality);
            }

            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
            writer.dispose();

            return baos.toByteArray();
        }
    }

    @Override
    public void saveToProduct(Product product, List<byte[]> images) throws ClassNotFoundException {
        List<Image> imagesToSave = images.stream()
                .map(populateNewImage(product))
                .collect(Collectors.toList());
        imageRepository.saveAll(imagesToSave);
    }

    @Override
    public void saveToProduct(Product product, byte[] image) throws ClassNotFoundException {
        Image toSave = populateNewImage(product).apply(image);
        imageRepository.save(toSave);
    }

    private Function<byte[], Image> populateNewImage(Product ownerProduct) {
        return bytes -> new Image(0, bytes, false, ownerProduct);
    }

    @Override
    public void removeById(List<Long> imageIdList) {
        imageIdList.forEach(this::removeById);
    }

    @Override
    public void removeById(long imageId) {
        imageRepository.deleteById(imageId);
    }
}