package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {
    @Mock
    private MailService mailService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    @InjectMocks
    private EmailController emailController;
    @Captor
    private ArgumentCaptor<UUID> captor;

    @BeforeEach
    void init() {
        messageSourceUtil.setMSource(messageSource);
    }

    @ParameterizedTest
    @ValueSource(strings = {"the code was not found", "the code does not exist"})
    public void validateEmail_whenCodeNotFound_shouldThrowEmailValidationCodeNotFoundException(String message) throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        Mockito.doThrow(new EmailValidationCodeNotFoundException(message)).when(mailService).validateEmail(any(UUID.class));

        assertThrows(EmailValidationCodeNotFoundException.class, () -> emailController.validateEmail(UUID.randomUUID()));
        verifyNoInteractions(messageSource);
    }

    @ParameterizedTest
    @ValueSource(strings = {"the code was expired", "the code was not accessible"})
    public void validateEmail_whenCodeExpired_shouldThrowEmailTokenExpiredException(String message) throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        Mockito.doThrow(new EmailValidationCodeExpiredException(message)).when(mailService).validateEmail(any(UUID.class));

        assertThrows(EmailValidationCodeExpiredException.class, () -> emailController.validateEmail(UUID.randomUUID()));
        verifyNoInteractions(messageSource);
    }

    @Test
    public void validateEmail_whenCodeExistAndUnexpired_shouldExecuteCorrectly() throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        UUID expected = UUID.randomUUID();

        Mockito.doNothing().when(mailService).validateEmail(expected);
        Mockito.doReturn("Your email was successfully activated").when(messageSource).getMessage(eq("email.confirmed"), any(), any());

        emailController.validateEmail(expected);
        verify(mailService).validateEmail(captor.capture());

        assertEquals(expected, captor.getValue());
    }


}