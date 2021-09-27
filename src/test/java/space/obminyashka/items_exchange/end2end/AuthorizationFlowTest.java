package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.DataConflictException;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.config.SecurityConfig.REFRESH_TOKEN;
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
    private static final String BEARER_PREFIX = "Bearer ";
    private final UserRegistrationDto userRegistrationDto = new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);

    @Autowired
    public AuthorizationFlowTest(MockMvc mockMvc) {
        super(mockMvc);
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
    void register_whenUserDataInvalid_shouldThrowException(UserRegistrationDto dto, ResultMatcher expectedStatus, String errorMessage, Class<Exception> resolvedException) throws Exception {
        final var result = sendDtoAndGetMvcResult(post(AUTH_REGISTER), dto, expectedStatus);

        assertThat(result.getResolvedException(), is(instanceOf(resolvedException)));
        assertTrue(result.getResponse().getContentAsString().contains(getMessageSource(errorMessage)));
    }

    private static Stream<Arguments> userRegistrationData() {
        return Stream.of(
                Arguments.of(new UserRegistrationDto(EXISTENT_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        status().isConflict(), "username-email.duplicate", DataConflictException.class),
                Arguments.of(new UserRegistrationDto(INVALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        status().isBadRequest(), "invalid.username", MethodArgumentNotValidException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, EXISTENT_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        status().isConflict(), "username-email.duplicate", DataConflictException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, INVALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD),
                        status().isBadRequest(), "invalid.email", MethodArgumentNotValidException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, INVALID_PASSWORD),
                        status().isBadRequest(), "different.passwords", MethodArgumentNotValidException.class),
                Arguments.of(new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, INVALID_PASSWORD, INVALID_PASSWORD),
                        status().isBadRequest(), "invalid.password", MethodArgumentNotValidException.class)
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
    @WithMockUser
    @DataSet(value = "auth/login.yml")
    void logout_Success_ShouldBeInvalidatedInInvalidatedTokensHolder_And_DeletedRefreshToken() throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header(REFRESH_TOKEN, BEARER_PREFIX + "refreshToken"), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("refresh.token.invalid").substring(0, 24)));
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
