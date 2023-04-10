package space.obminyashka.items_exchange.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.exception.IllegalIdentifierException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.IMAGE_BY_ADV_ID;
import static space.obminyashka.items_exchange.api.ApiKey.IMAGE_IN_ADV_COUNT;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ExceptionMessage.*;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerIntegrationTest extends BasicControllerTest {

    @MockBean
    private AdvertisementService advertisementService;
    @MockBean
    private ImageService imageService;
    @Mock
    private Advertisement advertisement;
    @Mock
    private User user;
    @Captor
    private ArgumentCaptor<List<byte[]>> listArgumentCaptor;
    private ArrayList<Image> testImages;
    private MockMultipartFile jpeg;
    private final UUID advertisementId = UUID.fromString("65e3ee49-5927-40be-aafd-0461ce45f295");

    @Autowired
    public ImageControllerIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @BeforeEach
    void setUp() throws IOException {
        user = new User();
        user.setStatus(Status.ACTIVE);
        jpeg = new MockMultipartFile("image", "image-jpeg.jpeg", MediaType.IMAGE_JPEG_VALUE, "image jpeg".getBytes());
        mocksInit();
    }

    private void mocksInit() throws IOException {

        when(advertisementService.existById(advertisementId)).thenReturn(true);
        when(advertisementService.findByIdAndOwnerUsername(advertisementId, "admin"))
                .thenReturn(Optional.of(advertisement));
        testImages = IntStream.range(0, 10)
                .collect(ArrayList::new, (images, value) -> images.add(new Image()), ArrayList::addAll);
        when(advertisement.getImages()).thenReturn(testImages);
        when(advertisement.getUser()).thenReturn(user);
        when(imageService.compress(jpeg)).thenReturn(jpeg.getBytes());
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldThrowExceptionWhenTotalAmountMoreThan10() throws Exception {
        MvcResult mvcResult = mockMvc.perform(multipart(IMAGE_BY_ADV_ID, advertisementId)
                        .file(jpeg)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        verify(advertisementService).existById(any());
        verify(advertisementService).findByIdAndOwnerUsername(any(), anyString());
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(ElementsNumberExceedException.class);
    }

    @Test
    void countImagesInAdvertisement() throws Exception {
        when(imageService.countImagesForAdvertisement(any())).thenReturn(10);

        final var mvcResult = sendUriAndGetMvcResult(get(IMAGE_IN_ADV_COUNT, advertisementId), status().isOk());
        assertEquals("10", mvcResult.getResponse().getContentAsString());
        verify(imageService).countImagesForAdvertisement(any());
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldSaveImagesWhenTotalAmountLessThan10() throws Exception {
        testImages.remove(0);

        sendUriAndGetMvcResult(multipart(IMAGE_BY_ADV_ID, advertisementId).file(jpeg), status().isOk());

        verify(imageService).compress(any(MultipartFile.class));
        verify(imageService).saveToAdvertisement(any(), listArgumentCaptor.capture());
        verify(advertisementService).findByIdAndOwnerUsername(any(), anyString());
        assertEquals(jpeg.getBytes(), listArgumentCaptor.getValue().get(0));
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn404WhenAdvertisementIsNotExist() throws Exception {
        mockMvc.perform(multipart(IMAGE_BY_ADV_ID, UUID.randomUUID())
                        .file(jpeg))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByAdvertisementId_shouldReturn404WhenAdvertisementIsNotExist() throws Exception {
        when(imageService.getByAdvertisementId(advertisementId)).thenReturn(new ArrayList<>());

        final var mvcResult = sendUriAndGetMvcResult(get(IMAGE_BY_ADV_ID, advertisementId), status().isNotFound());
        assertThat(mvcResult.getResolvedException()).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(getMessageSource(ADVERTISEMENT_NOT_EXISTED_ID));
    }

    @WithMockUser("admin")
    @Test
    void deleteImages_shouldThrow400WhenImageIdNotExist() throws Exception {
        final var randomID = UUID.randomUUID();
        final MvcResult mvcResult = sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, advertisementId)
                        .param("ids", randomID.toString()),
                status().isBadRequest());

        verify(advertisementService).findByIdAndOwnerUsername(any(UUID.class), eq("admin"));
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(IllegalIdentifierException.class)
                .hasMessage(getParametrizedMessageSource(IMAGE_NOT_EXISTED_ID, "[%s]".formatted(randomID)));
    }

    @WithMockUser
    @Test
    void deleteImages_shouldThrow403WhenUserNotOwnAdvertisement() throws Exception {
        final MvcResult mvcResult = sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, advertisementId)
                        .param("ids", UUID.randomUUID().toString()),
                status().isForbidden());

        verify(advertisementService).findByIdAndOwnerUsername(any(UUID.class), eq("user"));
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(IllegalOperationException.class)
                .hasMessage(getMessageSource(ResponseMessagesHandler.ValidationMessage.USER_NOT_OWNER));
    }
}
