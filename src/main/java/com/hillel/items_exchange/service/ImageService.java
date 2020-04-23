package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.ImageRepository;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.Image;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public List<String> getLinksByProductId(Long id) {
        return imageRepository.findByProductId(id).stream()
                .map(Image::getResourceUrl)
                .collect(Collectors.toList());
    }

    public List<ImageDto> getByProductId(Long id) {
        return mapImagesToDto(imageRepository.findByProductId(id));
    }

    private List<ImageDto> mapImagesToDto(Iterable<Image> images) {
        return modelMapper.map(images, new TypeToken<List<ImageDto>>() {}.getType());
    }
}