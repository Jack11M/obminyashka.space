package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.repository.ImageRepository;
import space.obminyashka.items_exchange.rest.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.rest.exception.UnsupportedMediaTypeException;
import space.obminyashka.items_exchange.rest.response.ImageView;
import space.obminyashka.items_exchange.repository.model.Image;
import space.obminyashka.items_exchange.util.BasicImageCreator;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceIntegrationTest extends BasicImageCreator{
    @MockBean
    private ImageRepository imageRepository;
    @Autowired
    private ImageService imageService;
    private Image jpeg;
    private MockMultipartFile testJpg;
    private MockMultipartFile testPng;
    private MockMultipartFile testTxt;

    @BeforeEach
    void setUp() throws IOException {
        jpeg = new Image("test jpeg".getBytes(), null);
        testJpg = getImageBytes(MediaType.IMAGE_JPEG);
        testPng = getImageBytes(MediaType.IMAGE_PNG);
        testTxt = new MockMultipartFile("image", "text.txt", MediaType.TEXT_PLAIN_VALUE, "plain text".getBytes());
    }

    @Test
    void getImagesResourceByAdvertisementId_shouldReturnAllImagesLinkedToAdvertisement_whenAdvertisementExistsAndContainsImages() {
        when(imageRepository.getImagesResourceByAdvertisementId(any()))
                .thenReturn(Collections.singletonList(jpeg.getResource()));

        List<byte[]> result = imageService.getImagesResourceByAdvertisementId(UUID.randomUUID());
        assertEquals(jpeg.getResource(), result.getFirst(), "Images' resources should be equal");
        verify(imageRepository).getImagesResourceByAdvertisementId(any());
    }

    @Test
    void getByAdvertisementId_shouldReturnPopulatedImageDto_whenAdvertisementExistsAndContainsImage() {
        when(imageRepository.findByAdvertisementId(any())).thenReturn(List.of(jpeg));

        ImageView imageView = imageService.getByAdvertisementId(UUID.randomUUID()).getFirst();
        assertAll("Checking objects' data equal",
                () -> assertEquals(jpeg.getId(), imageView.getId()),
                () -> assertArrayEquals(jpeg.getResource(), imageView.getResource()));
        verify(imageRepository).findByAdvertisementId(any());
    }

    @Test
    void compressImage_shouldCompressImage_WhenValidImageType() throws IOException {
        byte[] compressImage = imageService.compress(testJpg);
        assertTrue(testJpg.getBytes().length > compressImage.length);
    }

    @Test
    void compressImage_shouldThrowException_WhenInvalidImageType(){
        assertThrows(UndeclaredThrowableException.class, () -> imageService.compress(testTxt));
    }

    @Test
    void saveToAdvertisement_shouldSaveImageBytes_whenAdvertisementExists() throws ElementsNumberExceedException, UnsupportedMediaTypeException {
        List<MultipartFile> testImages = List.of(testJpg, testPng);

        imageService.saveToAdvertisement(UUID.randomUUID(), testImages);
        verify(imageRepository, times(testImages.size())).createImage(any(), any(), any());
    }

    @Test
    void removeById_shouldRemoveAllImagesWithReceivedId() {
        List<UUID> testImagesId = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        imageService.removeById(testImagesId);
        verify(imageRepository).deleteAllByIdIn(testImagesId);
        verifyNoMoreInteractions(imageRepository);
    }

    @Test
    void removeById_shouldRemoveOneImageWithReceivedId() {
        var imageId = UUID.randomUUID();
        imageService.removeById(imageId);
        verify(imageRepository).deleteById(imageId);
        verifyNoMoreInteractions(imageRepository);
    }

    @Test
    void scale_shouldReturnScaledImage() throws IOException {
        byte[] bytes = testPng.getBytes();
        byte[] result = imageService.scale(bytes);
        assertTrue(bytes.length >= result.length, "Images' resources should have a smaller size");
    }

    @Test
    void scale_shouldReturnScaledJpegImage() throws IOException {
        byte[] bytes = testJpg.getBytes();
        byte[] result = imageService.scale(bytes);
        assertTrue(bytes.length >= result.length, "Images' resources should have a smaller size");
    }
}