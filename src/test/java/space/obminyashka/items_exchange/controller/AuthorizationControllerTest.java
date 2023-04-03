package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthService authService;
    private AuthController authController;
    @Captor
    private ArgumentCaptor<EmailConfirmationToken> captor;
    private final UserRegistrationDto dto = new UserRegistrationDto("user", "user@mail.ua", "pass", "pass");
    private User userFromDto = new User();

    @BeforeEach
    void setUp() {
        BeanUtils.copyProperties(dto, userFromDto);
        messageSourceUtil.setMSource(mock(MessageSource.class));
    }

    @Test
    void register_whenAllServicesPositiveFlow_shouldReturnCreated() throws Exception {
        when(userService.registerNewUser(any())).thenReturn(true);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(userFromDto));

        final var responseEntity = authController.registerUser(dto);

        assertAll("Verify invoking services one by one and expected status",
                () -> verify(userService).existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()),
                () -> verify(mailService).sendMail(dto.getEmail(), EmailType.REGISTRATION, Locale.getDefault()),
                () -> verify(userService).registerNewUser(eq(dto), any()),
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()));
    }

    @Test
    void register_whenMailServiceFailed_shouldReturnServiceUnavailable() throws Exception {
        doThrow(new IOException("Expected exception!")).when(mailService).sendMail(anyString(), any(), any());

        final var responseEntity = authController.registerUser(dto);

        assertAll("Verify invoking services one by one and expected status",
                () -> verify(userService).existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()),
                () -> verify(mailService).sendMail(dto.getEmail(), EmailType.REGISTRATION, Locale.getDefault()),
                () -> verifyNoMoreInteractions(userService),
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode()));
    }
}
