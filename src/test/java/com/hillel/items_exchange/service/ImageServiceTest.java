package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.ImageRepository;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ImageServiceTest {
    @MockBean
    private ProductService productService;
    @MockBean
    private ImageRepository imageRepository;
    @Autowired
    private ImageService imageService;
    private Image jpeg;

    @BeforeEach
    void setUp() {
        jpeg = new Image(1, "test jpeg".getBytes(), false, null);
    }

    @Test
    void getImagesResourceByProductId_shouldReturnAllImagesLinkedToProduct_WhenProductExistsAndContainsImages() {
        when(imageRepository.findByProductId(any())).thenReturn(List.of(jpeg));

        List<byte[]> result = imageService.getImagesResourceByProductId(1L);
        assertEquals(jpeg.getResource(), result.get(0), "Images' resources should be equal");
        verify(imageRepository).findByProductId(anyLong());
    }

    @Test
    void getByProductId_shouldReturnPopulatedImageDto_WhenProductExistsAndContainsImage() {
        when(imageRepository.findByProductId(any())).thenReturn(List.of(jpeg));

        ImageDto imageDto = imageService.getByProductId(1L).get(0);
        assertAll("Checking objects' data equal",
                () -> assertEquals(jpeg.getId(), imageDto.getId()),
                () -> assertArrayEquals(jpeg.getResource(), imageDto.getResource()),
                () -> assertEquals(jpeg.isDefaultPhoto(), imageDto.isDefaultPhoto()));
        verify(imageRepository).findByProductId(anyLong());
    }

    @Test
    void compressImages() {

    }

    @Test
    void compressImage() {
    }

    @Test
    void saveToProduct() {
    }

    @Test
    void testSaveToProduct() {
    }

    @Test
    void removeById() {
    }

    @Test
    void testRemoveById() {
    }
}