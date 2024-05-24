package space.obminyashka.items_exchange.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.request.ValidationEmailRequest;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;


import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.EMAIL_RESEND_CODE;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;


@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class EmailControllerIntegrationTest extends BasicControllerTest {
    @Autowired
    public EmailControllerIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    public static final String INVALID_EMAIL_WITHOUT_POINT = "username@domain";
    public static final String INVALID_EMAIL_WITHOUT_DOMAIN_NAME = "username@.com";

    @ParameterizedTest
    @WithMockUser(username = "user")
    @MethodSource("listInvalidEmail")
    void resendValidationCode_whenEmailConfirmationWrong_shouldThrowIllegalArgumentException(String email) throws Exception {
        final var validationEmailRequest = new ValidationEmailRequest(email);

        MvcResult mvcResult = sendDtoAndGetMvcResult(post(EMAIL_RESEND_CODE), validationEmailRequest, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL)));
    }

    private static Stream<Arguments> listInvalidEmail() {
        return Stream.of(
                Arguments.of(INVALID_EMAIL_WITHOUT_POINT),
                Arguments.of(INVALID_EMAIL_WITHOUT_DOMAIN_NAME)
        );
    }
}
