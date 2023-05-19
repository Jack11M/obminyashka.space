package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

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
        when(userService.findByUsernameOrEmail(anyString())).thenReturn(Optional.of(mock(User.class)));
        when(imageService.scale(bmp)).thenAnswer((t) -> {
            throw new IOException(getMessageSource(ResponseMessagesHandler.ValidationMessage.INCORRECT_IMAGE_FORMAT));
        });

        assertThrows(IOException.class, () -> userController.updateUserAvatar(bmp, authentication));
    }
}
