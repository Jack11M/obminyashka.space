package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.dao.EmailConfirmationTokenRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.model.EmailConfirmationToken;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.impl.SendGridService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    public static final UUID id = UUID.fromString("61731cc8-8104-49f0-b2c3-5a52e576ab18");
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    @Captor
    private ArgumentCaptor<UUID> userIdArgumentCaptor;
    @InjectMocks
    private SendGridService sendGridService;

    @Test
    public void TestValidateEmailCode_WhenDataCorrect_Successfully() throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        when(emailConfirmationTokenRepository.findById(id)).thenReturn(creatEmailConfirmToken());
        sendGridService.validateEmail(id);

        verify(userRepository).setValidatedEmailToUserById(userIdArgumentCaptor.capture());
        assertEquals(id, userIdArgumentCaptor.getValue());
    }

    private Optional<EmailConfirmationToken> creatEmailConfirmToken() {
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();

        LocalDateTime localDateTime = LocalDateTime.now();
        emailConfirmationToken.setExpiryDate(localDateTime.plusDays(1L));

        User user = new User();
        user.setId(id);
        user.setEmail("user@mail.ua");
        emailConfirmationToken.setUser(user);

        return Optional.of(emailConfirmationToken);
    }
}
