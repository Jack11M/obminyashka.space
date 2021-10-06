package space.obminyashka.items_exchange.controller;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest extends BasicControllerTest {

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

    @Autowired
    public ImageControllerTest(MockMvc mockMvc) {
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
        when(advertisementService.existById(1L)).thenReturn(true);
        when(advertisementService.findByIdAndOwnerUsername(1L, "admin")).thenReturn(Optional.of(advertisement));
        testImages = IntStream.range(0, 10)
                .collect(ArrayList::new, (images, value) -> images.add(new Image()), ArrayList::addAll);
        when(advertisement.getImages()).thenReturn(testImages);
        when(advertisement.getUser()).thenReturn(user);
        when(imageService.compress(jpeg)).thenReturn(jpeg.getBytes());
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldThrowExceptionWhenTotalAmountMoreThan10() throws Exception {
        MvcResult mvcResult = mockMvc.perform(multipart(IMAGE_BY_ADV_ID, 1L)
                .file(jpeg)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();

        verify(advertisementService).existById(anyLong());
        verify(advertisementService).findByIdAndOwnerUsername(anyLong(), anyString());
        assertThat(mvcResult.getResolvedException(), is(instanceOf(ElementsNumberExceedException.class)));
    }

    @Test
    void countImagesInAdvertisement() throws Exception {
        when(imageService.countImagesForAdvertisement(1L)).thenReturn(10);

        final var mvcResult = sendUriAndGetMvcResult(get(IMAGE_COUNT, 1L), status().isOk());
        assertEquals("10", mvcResult.getResponse().getContentAsString());
        verify(imageService).countImagesForAdvertisement(anyLong());
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldSaveImagesWhenTotalAmountLessThan10() throws Exception {
        testImages.remove(0);

        sendUriAndGetMvcResult(multipart(IMAGE_BY_ADV_ID, 1L).file(jpeg), status().isOk());

        verify(imageService).compress(any(MultipartFile.class));
        verify(imageService).saveToAdvertisement(any(), listArgumentCaptor.capture());
        verify(advertisementService).findByIdAndOwnerUsername(anyLong(), anyString());
        assertEquals(jpeg.getBytes(), listArgumentCaptor.getValue().get(0));
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn404WhenAdvertisementIsNotExist() throws Exception {
        mockMvc.perform(multipart(IMAGE_BY_ADV_ID, 50L)
                .file(jpeg))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser("admin")
    @Test
    void deleteImages_shouldThrow400WhenImageIdNotExist() throws Exception {
        final MvcResult mvcResult = sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, 1L)
                        .param("ids", "999"),
                status().isBadRequest());

        verify(advertisementService).findByIdAndOwnerUsername(1L, "admin");
        verifyResultException(mvcResult, IllegalIdentifierException.class, getParametrizedMessageSource("exception.image.not-existed-id", "[999]"));
    }

    @WithMockUser
    @Test
    void deleteImages_shouldThrow403WhenUserNotOwnAdvertisement() throws Exception {
        final MvcResult mvcResult = sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, 1L)
                        .param("ids", "1"),
                status().isForbidden());

        verify(advertisementService).findByIdAndOwnerUsername(1L, "user");
        verifyResultException(mvcResult, IllegalOperationException.class, getMessageSource("user.not-owner"));
    }
}
