package space.obminyashka.items_exchange.rest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.request.ChangeEmailRequest;
import space.obminyashka.items_exchange.rest.request.ValidationEmailRequest;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.impl.ImageServiceImpl;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.EMAIL_RESEND_CODE;
import static space.obminyashka.items_exchange.rest.api.ApiKey.USER_SERVICE_CHANGE_EMAIL;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.util.data_producer.UserDtoCreatingUtil.INVALID_EMAIL_WITHOUT_DOMAIN_NAME;
import static space.obminyashka.items_exchange.util.data_producer.UserDtoCreatingUtil.INVALID_EMAIL_WITHOUT_POINT;

@SpringBootTest
@AutoConfigureMockMvc
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
