package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.security.jwt.InvalidatedTokensHolder;
import space.obminyashka.items_exchange.security.jwt.refresh.RefreshTokenRequest;

import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@SpringBootTest
@DBRider
@DataSet("database_init.yml")
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class AuthorizationFlowTest extends BasicControllerTest {

    protected static final String VALID_USERNAME = "test";
    protected static final String VALID_EMAIL = "test@test.com";
    protected static final String VALID_PASSWORD = "Test!1234";
    protected static final String EXISTENT_USERNAME = "admin";
    protected static final String EXISTENT_EMAIL = "admin@gmail.com";
    protected static final String INVALID_PASSWORD = "test123456";
    protected static final String INVALID_EMAIL = "email.com";
    protected static final String INVALID_USERNAME = "user name";
    private final UserRegistrationDto userRegistrationDto = new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    private final InvalidatedTokensHolder invalidatedTokensHolder;

    @Autowired
    public AuthorizationFlowTest(MockMvc mockMvc, InvalidatedTokensHolder invalidatedTokensHolder) {
        super(mockMvc);
        this.invalidatedTokensHolder = invalidatedTokensHolder;
    }

    @Test
    @Commit
    @ExpectedDataSet(value = "auth/register_user.yml", ignoreCols = {"password", "created", "updated", "last_online_time"})
    void register_shouldCreateValidNewUserAndReturnCreated() throws Exception {
        sendDtoAndGetMvcResult(post(AUTH_REGISTER), userRegistrationDto, status().isCreated());
    }

    @Test
    void register_whenDtoIsValid_shouldReturnSpecificSuccessMessage() throws Exception {
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER), userRegistrationDto, status().isCreated());

        String seekingResponse = getMessageSource("user.created");
        assertTrue(result.getResponse().getContentAsString().contains(seekingResponse));
    }

    @ParameterizedTest
    @MethodSource("userRegistrationData")
    void register_whenUserDataInvalid_shouldThrowException(UserRegistrationDto dto, String errorMessage, Class<Exception> resolvedException) throws Exception {
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER), dto, status().isBadRequest());

        assertThat(result.getResolvedException(), is(instanceOf(resolvedException)));
        assertTrue(result.getResponse().getContentAsString().contains(getMessageSource(errorMessage)));
    }

    private static Stream<Arguments> userRegistrationData() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(EXISTENT_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD), "username.duplicate", UndeclaredThrowableException.class),
                Arguments.of(new UserRegistrationDto(INVALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD), "invalid.username", ConstraintViolationException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, EXISTENT_EMAIL, VALID_PASSWORD, VALID_PASSWORD), "email.duplicate", UndeclaredThrowableException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, INVALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD), "invalid.email", ConstraintViolationException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, INVALID_PASSWORD), "different.passwords", ConstraintViolationException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, INVALID_PASSWORD, INVALID_PASSWORD), "invalid.password", ConstraintViolationException.class)
        );
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void login_Success_shouldReturnHttpOk() throws Exception {
        sendDtoAndGetResultAction(post(AUTH_LOGIN), new UserLoginDto(VALID_USERNAME, VALID_PASSWORD), status().isOk())
                .andExpect(content().json("{'firstname':'firstname'}"))
                .andExpect(content().json("{'lastname':'lastname'}"))
                .andExpect(content().json("{'email':'test@test.com'}"))
                .andExpect(content().json("{'avatarImage':'dGVzdCBpbWFnZSBwbmc='}"));
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void logout_Success_ShouldBeInvalidatedInInvalidatedTokensHolder_And_DeletedRefreshToken() throws Exception {
        final var result = sendDtoAndGetMvcResult(post(AUTH_LOGIN), new UserLoginDto(VALID_USERNAME, VALID_PASSWORD),
                status().isOk());
        final String accessToken = getJsonPathValue(result, "$.accessToken");
        final String refreshToken = getJsonPathValue(result, "$.refreshToken");
        sendUriAndGetResultAction(post(AUTH_LOGOUT)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken), status().isNoContent());
        assertTrue(invalidatedTokensHolder.isInvalidated(accessToken));

        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_REFRESH_TOKEN),
                new RefreshTokenRequest(refreshToken), status().isNotFound());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("refresh.token.not-found").substring(0, 13)));
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void logout_Failure_ShouldThrowJwtExceptionAfterRequestWithInvalidToken() throws Exception {
        final String token = "DefinitelyNotValidToken";
        sendUriAndGetMvcResult(post(AUTH_LOGOUT)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token), status().isUnauthorized());
    }

    private String getJsonPathValue(MvcResult result, String key) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), key);
    }
}
