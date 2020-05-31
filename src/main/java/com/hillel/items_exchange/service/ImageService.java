package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.ImageRepository;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.Image;
import com.hillel.items_exchange.model.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ImageRepository imageRepository;

    public List<byte[]> getImageResources(Long id) {
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

    public void saveToProduct(long productId, List<MultipartFile> photos) throws ClassNotFoundException, IOException {
        Product hostProduct = productService.findById(productId).orElseThrow(ClassNotFoundException::new);
        List<Image> imagesToSave = extractBytes(photos).stream()
                .map(bytes -> new Image(0, bytes, false, hostProduct))
                .collect(Collectors.toList());
        imageRepository.saveAll(imagesToSave);
    }

    public void removeById(List<Long> imageIdList) {
        imageIdList.forEach(imageRepository::deleteById);
    }

    private List<byte[]> extractBytes(List<MultipartFile> files) throws IOException {
        List<byte[]> convertedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            convertedFiles.add(file.getBytes());
        }
        return convertedFiles;
    }
}