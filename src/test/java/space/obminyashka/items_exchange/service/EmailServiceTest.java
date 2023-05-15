package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;
import space.obminyashka.items_exchange.service.impl.SendGridService;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    public static final UUID EXPECTED_ID = UUID.fromString("61731cc8-8104-49f0-b2c3-5a52e576ab18");

    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private EmailConfirmationCodeRepository emailConfirmationCodeRepository;
    @Captor
    private ArgumentCaptor<UUID> IdArgumentCaptor;
    @InjectMocks
    private SendGridService sendGridService;
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;

    @BeforeEach
    void init() {
        messageSourceUtil.setMSource(messageSource);
    }

    @Test
    void validateEmail_whenConfirmationCodeFound_thenShouldReturn() throws EmailValidationCodeNotFoundException {
        final var exceptedConfirmCode = new EmailConfirmationCode(EXPECTED_ID, null, 1);
        when(emailConfirmationCodeRepository.findById(EXPECTED_ID)).thenReturn(Optional.of(exceptedConfirmCode));
        sendGridService.validateEmail(EXPECTED_ID);

        verify(userRepository).setValidatedEmailToUserByEmailId(IdArgumentCaptor.capture());
        assertEquals(exceptedConfirmCode.getId(), IdArgumentCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("exception_whenEmailNotFoundOrExpiryDateOut")
    void testValidateEmail_whenServiceThrewException_shouldCatchException(EmailConfirmationCode email, Exception expectedException) {
        when(emailConfirmationCodeRepository.findById(EXPECTED_ID)).thenReturn(Optional.ofNullable(email));
        assertThrows(expectedException.getClass(), () -> sendGridService.validateEmail(EXPECTED_ID));
    }

    private static Stream<Arguments> exception_whenEmailNotFoundOrExpiryDateOut() {
        return Stream.of(
                Arguments.of(null, new EmailValidationCodeNotFoundException("the code was not found")),
                Arguments.of(new EmailConfirmationCode(EXPECTED_ID, null, -1), new EmailValidationCodeNotFoundException("the code was expired")));
    }
}
