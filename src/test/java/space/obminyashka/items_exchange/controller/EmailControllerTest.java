package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

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
    private ArgumentCaptor<UUID> uuidCaptor;
    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @BeforeEach
    void init() {
        messageSourceUtil.setMSource(messageSource);
    }

    @ParameterizedTest
    @MethodSource("getTrowingExceptionsAndExpectedExceptions")
    public void validateEmail_whenServiceTrowedException_shouldCatchException(Exception exception, Class clazz) throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        Mockito.doThrow(exception).when(mailService).validateEmail(any(UUID.class));

        assertThrows(clazz, () -> emailController.validateEmail(UUID.randomUUID()));
        verifyNoInteractions(MessageSourceUtil.getMessageSource("email.confirmed"));
    }

    private static Stream<Arguments> getTrowingExceptionsAndExpectedExceptions() {
        return Stream.of(
                Arguments.of(new EmailValidationCodeNotFoundException("the code was not found"), EmailValidationCodeNotFoundException.class),
                Arguments.of(new EmailValidationCodeExpiredException("the code was expired"), EmailValidationCodeExpiredException.class)
        );
    }

    @Test
    public void validateEmail_whenCodeExistAndUnexpired_shouldExecuteCorrectly() throws EmailValidationCodeExpiredException, EmailValidationCodeNotFoundException {
        UUID expectedUUID = UUID.randomUUID();
        String expectedMessageProperty = "email.confirmed";

        Mockito.doReturn("Your email was successfully activated").when(messageSource).getMessage(eq(expectedMessageProperty), any(), any());

        emailController.validateEmail(expectedUUID);
        verify(mailService).validateEmail(uuidCaptor.capture());
        verify(messageSource).getMessage(stringCaptor.capture(), any(), any());

        assertEquals(expectedUUID, uuidCaptor.getValue());
        assertEquals(expectedMessageProperty, stringCaptor.getValue());
    }
}