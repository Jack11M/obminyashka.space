package space.obminyashka.items_exchange.controller;

import org.apache.http.HttpHeaders;
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
import space.obminyashka.items_exchange.exception.EmailSendingException;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import static org.junit.jupiter.api.Assertions.*;
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
    private UserRegistrationDto dto;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, userService, authService, mailService);
        dto = new UserRegistrationDto("user", "user@mail.ua", "pass", "pass");
        messageSourceUtil.setMSource(mock(MessageSource.class));
    }

    @Test
    void register_whenAllServicesPositiveFlow_shouldReturnCreated() throws Exception {
        when(userService.registerNewUser(any(), any())).thenReturn(true);

        final var responseEntity = authController.registerUser(dto, HttpHeaders.HOST);

        assertAll("Verify invoking services one by one and expected status",
                () -> verify(userService).existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()),
                () -> verify(mailService).sendEmailTemplateAndGenerateConfrimationCode(eq(dto.getEmail()), eq(EmailType.REGISTRATION), any()),
                () -> verify(userService).registerNewUser(eq(dto), any()),
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()));
    }

    @Test
    void register_whenMailServiceFailed_shouldThrowEmailSendingException() throws Exception {
        doThrow(new EmailSendingException("Expected exception!")).when(mailService).sendEmailTemplateAndGenerateConfrimationCode(anyString(), any(), any());


        assertThrows(EmailSendingException.class, ()->authController.registerUser(dto, HttpHeaders.HOST));

        assertAll("Verify invoking services one by one and expected status",
                () -> verify(userService).existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()),
                () -> verify(mailService).sendEmailTemplateAndGenerateConfrimationCode(eq(dto.getEmail()), eq(EmailType.REGISTRATION), any()),
                () -> verifyNoMoreInteractions(userService));
    }
}