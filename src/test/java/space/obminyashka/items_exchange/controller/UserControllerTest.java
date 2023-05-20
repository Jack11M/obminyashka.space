package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.UserService;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private ImageService imageService;
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private Authentication authentication;
    @Captor
    private ArgumentCaptor<byte[]> captorAvatar;
    @Captor
    private ArgumentCaptor<String> captorName;
    @InjectMocks
    private UserController userController;
    private final MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
    private final MockMultipartFile jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE, "image jpg".getBytes());

    @Test
    void updateUserAvatar_WhenAuthorized_ShouldSetAvatar() throws IOException {
        when(imageService.scale(jpeg)).thenReturn(jpeg.getBytes());

        Map<String, byte[]> result = userController.updateUserAvatar(jpeg, authentication);

        assertAll("Validate all operations with image were invoked",
                () -> assertEquals(Map.of("avatarImage", jpeg.getBytes()), result),
                () -> verify(userService).setUserAvatar(captorName.capture(), captorAvatar.capture()),
                () -> assertEquals(authentication.getName(), captorName.getValue()),
                () -> assertEquals(jpeg.getBytes(), captorAvatar.getValue()));
    }

    @Test
    void updateUserAvatar_WhenReceivedBMPImage_ShouldThrowIOException() {
        when(imageService.scale(bmp)).thenAnswer((t) -> {
            throw new IOException("Incorrect image format");
        });

        assertThrows(IOException.class, () -> userController.updateUserAvatar(bmp, authentication));
    }
}
