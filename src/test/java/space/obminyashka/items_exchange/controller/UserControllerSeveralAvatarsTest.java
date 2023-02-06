package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.USER_SERVICE_CHANGE_AVATAR;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.OLD_USER_VALID_EMAIL;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerSeveralAvatarsTest extends BasicControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ImageService imageService;

    private User user;

    @Autowired
    public UserControllerSeveralAvatarsTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    @WithMockUser(username = "admin")
    void setUserAvatar_whenReceivedSeveralImages_shouldSaveFirstImage() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        MockMultipartFile jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE, "image jpg".getBytes());

        when(imageService.compress((MultipartFile) any())).thenReturn(jpeg.getBytes());

        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);

        sendUriAndGetMvcResult(multipart(new URI(USER_SERVICE_CHANGE_AVATAR)).file(jpeg).file(bmp), status().is2xxSuccessful());

        Mockito.verify(imageService).compress(captor.capture());

        MultipartFile actualImage = captor.getValue();

        assertEquals(jpeg.getName(), actualImage.getName());
    }

    private User createUser() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail(OLD_USER_VALID_EMAIL);
        user.setStatus(Status.ACTIVE);
        user.setLastOnlineTime(LocalDateTime.now());
        user.setChildren(Collections.emptyList());
        user.setPhones(Set.of(new Phone(UUID.randomUUID(), +381234567890L, true, user)));

        return user;
    }
}
