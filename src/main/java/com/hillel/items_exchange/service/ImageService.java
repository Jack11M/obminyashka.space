package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.ImageRepository;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.Image;
import com.hillel.items_exchange.model.Product;
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
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ImageRepository imageRepository;

    public List<byte[]> getImagesResourceByProductId(Long id) {
        return imageRepository.findByProductId(id).stream()
                .map(Image::getResource)
                .collect(Collectors.toList());
    }

    public List<ImageDto> getByProductId(Long id) {
        return mapImagesToDto(imageRepository.findByProductId(id));
    }

    private List<ImageDto> mapImagesToDto(Iterable<Image> images) {
        return modelMapper.map(images, new TypeToken<List<ImageDto>>() {}.getType());
    }

    public List<ByteArrayInputStream> compressImages(List<MultipartFile> photos) throws IOException {
        List<ByteArrayInputStream> compressedImages = new ArrayList<>();
        for (MultipartFile photo : photos) {
            compressedImages.add(compressImage(photo));
        }
        return compressedImages;
    }

    public ByteArrayInputStream compressImage(MultipartFile photo) throws IOException {

        String contentType = photo.getContentType();
        if (contentType == null || contentType.equals(MediaType.IMAGE_GIF_VALUE)) {
            return new ByteArrayInputStream(photo.getBytes());
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(baos);
             ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {

            ImageWriter writer = ImageIO.getImageWritersByFormatName(contentType).next();
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                float quality = 0.05f;
                param.setCompressionQuality(quality);
            }

            writer.write(null, new IIOImage(ImageIO.read(photo.getInputStream()), null, null), param);
            writer.dispose();

            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    public void saveToProduct(long productId, List<ByteArrayInputStream> photos) throws ClassNotFoundException {
        Product hostProduct = productService.findById(productId).orElseThrow(ClassNotFoundException::new);
        List<Image> imagesToSave = extractBytes(photos).stream()
                .map(bytes -> new Image(0, bytes, false, hostProduct))
                .collect(Collectors.toList());
        imageRepository.saveAll(imagesToSave);
    }

    public void removeById(List<Long> imageIdList) {
        imageIdList.forEach(imageRepository::deleteById);
    }

    private List<byte[]> extractBytes(List<ByteArrayInputStream> files) {
        return files.stream()
                .map(ByteArrayInputStream::readAllBytes)
                .collect(Collectors.toList());
    }
}