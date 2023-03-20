package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.exception.EmailTokenExpiredException;
import space.obminyashka.items_exchange.exception.EmailTokenNotExistsException;
import space.obminyashka.items_exchange.service.MailService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {
    @Mock
    private MailService mailService;
    @InjectMocks
    private EmailController emailController;

    @Test
    public void validateEmailAndThenActivate_WhenTokenNotFound_ShouldThrowEmailTokenNotExistsException() throws EmailTokenExpiredException, EmailTokenNotExistsException {
        Mockito.doThrow(new EmailTokenNotExistsException("token is not exist")).when(mailService).validateEmail(any(UUID.class));

        assertThrows(EmailTokenNotExistsException.class, () -> {
            emailController.validateEmailAndThenActivate(UUID.randomUUID());
        });
    }

    @Test
    public void validateEmailAndThenActivate_WhenTokenExpired_ShouldThrowEmailTokenExpiredException() throws EmailTokenExpiredException, EmailTokenNotExistsException {
        Mockito.doThrow(new EmailTokenExpiredException("token is expired")).when(mailService).validateEmail(any(UUID.class));

        assertThrows(EmailTokenExpiredException.class, () -> {
            emailController.validateEmailAndThenActivate(UUID.randomUUID());
        });
    }

    @Test
    public void validateEmailAndThenActivate_WhenTokenExistAndUnexpired_ShouldExecuteCorrectly() throws EmailTokenExpiredException, EmailTokenNotExistsException {
        Mockito.doNothing().when(mailService).validateEmail(any(UUID.class));

       emailController.validateEmailAndThenActivate(UUID.randomUUID());
    }
}