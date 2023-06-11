package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.EmailSendingException;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@SpringBootTest
@DBRider
@DataSet("database_init.yml")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorizationFlowTest extends BasicControllerTest {

    protected static final String VALID_USERNAME = "test";
    protected static final String VALID_EMAIL = "test@test.com";
    protected static final String VALID_PASSWORD = "Test!1234";
    protected static final String EXISTENT_USERNAME = "admin";
    protected static final String EXISTENT_EMAIL = "admin@gmail.com";
    protected static final String INVALID_PASSWORD = "test123456";
    protected static final String INVALID_EMAIL = "email.com";
    protected static final String INVALID_USERNAME = "user name";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DOMAIN_URL = "https://obminyashka.space";
    @MockBean
    private SendGrid sendGrid;
    private final UserRegistrationDto userRegistrationDto = new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    private final EmailConfirmationCodeRepository emailConfirmationCodeRepository;

    @Autowired
    public AuthorizationFlowTest(MockMvc mockMvc, EmailConfirmationCodeRepository emailConfirmationCodeRepository) {
        super(mockMvc);
        this.emailConfirmationCodeRepository = emailConfirmationCodeRepository;
    }

    @Test
    @Commit
    @ExpectedDataSet(
            value = "auth/register_user.yml",
            orderBy = {"created", "name"},
            ignoreCols = {"id", "password", "created", "updated", "last_online_time"})
    void register_shouldCreateValidNewUserAndReturnCreated() throws Exception {
        when(sendGrid.api(any())).thenReturn(new Response(200, null, null));
        long codesCountBeforeRegister = emailConfirmationCodeRepository.count();
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER).header(HttpHeaders.HOST, DOMAIN_URL), userRegistrationDto, status().isCreated());
        long codesCountAfterRegister = emailConfirmationCodeRepository.count();

        String seekingResponse = getMessageSource(ResponseMessagesHandler.ValidationMessage.USER_CREATED);
        assertTrue(result.getResponse().getContentAsString().contains(seekingResponse));
        assertEquals(1, codesCountAfterRegister - codesCountBeforeRegister);
    }

    @Test
    void register_shouldCreateNewUserAndSendMail() throws Exception {
        when(sendGrid.api(any())).thenReturn(new Response(200, null, null));

        sendDtoAndGetMvcResult(post(AUTH_REGISTER).header(HttpHeaders.HOST, DOMAIN_URL), userRegistrationDto, status().isCreated());

        verify(sendGrid).api(any());
        Response sendGridApi = sendGrid.api(any());
        assertEquals(HttpStatus.OK.value(), sendGridApi.getStatusCode());
    }

    @Test
    void register_whenSendGridFailed_shouldReturnServiceUnavailable() throws Exception {
        doThrow(new IOException("Expected exception!")).when(sendGrid).api(any());
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER).header(HttpHeaders.HOST, DOMAIN_URL), userRegistrationDto, status().isServiceUnavailable());

        verify(sendGrid).api(any());

        assertThat(result.getResolvedException())
                .isInstanceOf(EmailSendingException.class)
                .hasMessageContaining(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_SENDING));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void register_whenUserRegistrationDtoIsEmpty_shouldReturnBadRequest(String uriVar) throws Exception {
        sendUriAndGetMvcResult(post(AUTH_REGISTER, uriVar), status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("userRegistrationConflictData")
    void register_whenUserRegistrationDtoIsEmpty_shouldReturnConflictRequest(UserRegistrationDto dto, String errorMessage) throws Exception {
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER).header(HttpHeaders.HOST, DOMAIN_URL), dto, status().isConflict());

        assertTrue(result.getResponse().getContentAsString().contains(getMessageSource(errorMessage)));
    }

    private static Stream<Arguments> userRegistrationConflictData() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(EXISTENT_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        ResponseMessagesHandler.ValidationMessage.USERNAME_EMAIL_DUPLICATE),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, EXISTENT_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        ResponseMessagesHandler.ValidationMessage.USERNAME_EMAIL_DUPLICATE)
        );
    }

    @ParameterizedTest
    @MethodSource("userRegistrationData")
    void register_whenUserDataInvalid_shouldThrowException(UserRegistrationDto dto, ResultMatcher expectedStatus, String errorMessage, Class<Exception> resolvedException) throws Exception {
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER).header(HttpHeaders.HOST, DOMAIN_URL), dto, expectedStatus);

        assertThat(result.getResolvedException())
                .isInstanceOf(resolvedException)
                .hasMessageContaining(getMessageSource(errorMessage));
    }

    private static Stream<Arguments> userRegistrationData() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(INVALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        status().isBadRequest(), ResponseMessagesHandler.ValidationMessage.INVALID_USERNAME, MethodArgumentNotValidException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, INVALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        status().isBadRequest(), ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL, MethodArgumentNotValidException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, INVALID_PASSWORD),
                        status().isBadRequest(), ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS, MethodArgumentNotValidException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, INVALID_PASSWORD, INVALID_PASSWORD),
                        status().isBadRequest(), ResponseMessagesHandler.ValidationMessage.INVALID_PASSWORD, MethodArgumentNotValidException.class)
        );
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void login_whenUserLoginViaEmail_shouldReturnHttpOk() throws Exception {
        sendDtoAndGetResultAction(post(AUTH_LOGIN), new UserLoginDto(VALID_EMAIL, VALID_PASSWORD), status().isOk())
                .andExpect(content().json("{\"username\": \"test\"}"));

    }

    @Test
    @DataSet(value = "auth/login.yml")
    void login_Success_shouldReturnHttpOk() throws Exception {
        sendDtoAndGetResultAction(post(AUTH_LOGIN), new UserLoginDto(VALID_USERNAME, VALID_PASSWORD), status().isOk())
                .andExpect(content().json("{\"firstname\":\"firstname\"}"))
                .andExpect(content().json("{\"lastname\":\"lastname\"}"))
                .andExpect(content().json("{\"email\":\"test@test.com\"}"))
                .andExpect(content().json("{\"avatarImage\":\"dGVzdCBpbWFnZSBwbmc=\"}"));
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void logout_Success_ShouldBeInvalidatedInInvalidatedTokensHolder_And_DeletedRefreshToken() throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header("refresh", BEARER_PREFIX + "refreshToken"), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_REFRESH_TOKEN).substring(0, 24)));
    }

    @Test
    @WithMockUser("test")
    @DataSet(value = "auth/login.yml")
    void logout_Failure_ShouldThrowJwtExceptionAfterRequestWithInvalidToken() throws Exception {
        final String token = "DefinitelyNotValidToken";
        sendUriAndGetMvcResult(post(AUTH_LOGOUT)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token), status().isUnauthorized());
    }
}
