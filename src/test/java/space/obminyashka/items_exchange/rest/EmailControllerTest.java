package space.obminyashka.items_exchange.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.rest.exception.not_found.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy;
import space.obminyashka.items_exchange.service.MailService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.PositiveMessage.EMAIL_CONFIRMED;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {
    @Mock
    private MailService mailService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private MessageSourceProxy messageSourceProxy;
    @InjectMocks
    private EmailController emailController;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @BeforeEach
    void init() {
        messageSourceProxy.setMSource(messageSource);
    }

    @Test
    void validateEmail_whenValidationCodeNotFound_shouldDoNothing()
            throws EmailValidationCodeNotFoundException {
        Mockito.doThrow(EmailValidationCodeNotFoundException.class).when(mailService).validateEmail(any(UUID.class));

        assertThrows(EmailValidationCodeNotFoundException.class, () -> emailController.validateEmail(UUID.randomUUID()));

        verifyNoInteractions(messageSource);
    }

    @Test
    void validateEmail_whenCodeExistAndUnexpired_shouldExecuteCorrectly()
            throws EmailValidationCodeNotFoundException {
        when(messageSource.getMessage(eq(EMAIL_CONFIRMED), any(), any()))
                .thenReturn("Your email was successfully activated");

        UUID expectedUUID = UUID.randomUUID();

        emailController.validateEmail(expectedUUID);

        verify(mailService).validateEmail(uuidCaptor.capture());
        verify(messageSource).getMessage(stringCaptor.capture(), any(), any());

        assertEquals(expectedUUID, uuidCaptor.getValue());
        assertEquals(EMAIL_CONFIRMED, stringCaptor.getValue());
    }
}