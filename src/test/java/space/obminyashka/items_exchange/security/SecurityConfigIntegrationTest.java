package space.obminyashka.items_exchange.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserLoginDto;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.config.SecurityConfig.ACCESS_TOKEN;
import static space.obminyashka.items_exchange.config.SecurityConfig.REFRESH_TOKEN;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class SecurityConfigIntegrationTest extends BasicControllerTest {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "@kuIOIY*h986";
    private static final String NOT_VALID_USERNAME = "nimda";
    private static final String NOT_VALID_PASSWORD = "drowssap";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_TOKEN = "DefinitelyNotValidToken";

    @Value("${app.access.jwt.expiration.time.ms}")
    private long accessJwtExpirationTime;

    @Value("${app.refresh.jwt.expiration.time.seconds}")
    private long refreshTokenExpirationTime;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public SecurityConfigIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @DataSet("database_init.yml")
    void loginWithValidUserIsOk() throws Exception {
        sendDtoAndGetResultAction(post(AUTH_LOGIN), createValidUserLoginDto(), status().isOk())
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.refresh_token").isNotEmpty());
    }

    @Test
    @DataSet("database_init.yml")
    void loginWithNotValidUserGetsBadRequest() throws Exception {
        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_LOGIN), createNotValidUserLoginDto(), status().isBadRequest());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("invalid.username-or-password")));
    }

    @Test
    void createAdvertisementWithoutTokenIsUnauthorized() throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(post(ADV), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("invalid.token")));
    }

    @Test
    @DataSet("database_init.yml")
    void createAdvertisementWithValidTokenWithoutAdvertisementDtoIsBadRequest() throws Exception {
        sendUriWithHeadersAndGetResultAction((post(ADV)), status().isBadRequest(),
                getAuthorizationHeaderWithValidToken());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithJWTTokenAndEmptyBodyShouldReturnBadRequest() throws Exception {
        final String tokenWithoutBearerPrefix = obtainToken(createValidUserLoginDto());
        final var mvcResult = sendUriWithHeadersAndGetMvcResult(multipart(ADV), status().isBadRequest(),
                getAuthorizationHeader(tokenWithoutBearerPrefix));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Required request part 'dto' is not present"));
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithNotValidJWTTokenIsUnauthorizedAndBadTokenSignature() throws Exception {
        final var invalidToken = BEARER_PREFIX + obtainToken(createValidUserLoginDto()).replaceAll(".$", "");
        final var mvcResult = sendUriWithHeadersAndGetMvcResult(post(ADV), status().isUnauthorized(),
                getAuthorizationHeader(invalidToken));
        assertEquals(getMessageSource("token.signature.not.valid"), mvcResult.getResponse().getContentAsString().trim());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithExpiredAccessJwtIsUnauthorizedAndAccessJwtIsExpired() throws Exception {
        final var headers = getAuthorizationHeaderWithValidToken();
        TimeUnit.MILLISECONDS.sleep(accessJwtExpirationTime);
        final var mvcResult = sendUriWithHeadersAndGetMvcResult(post(ADV), status().isUnauthorized(), headers);
        assertTrue(mvcResult.getResponse().getContentAsString().startsWith(getMessageSource("access.token.expired")));
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestToRefresh_shouldReturnNewAccessToken() throws Exception {
        final var refreshToken = getRefreshTokenValue();
        TimeUnit.MILLISECONDS.sleep(accessJwtExpirationTime);
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header(REFRESH_TOKEN, BEARER_PREFIX + refreshToken), status().isOk());
        final var newAccessToken = objectMapper.readTree(mvcResult.getResponse().getContentAsString())
                .get(ACCESS_TOKEN).textValue();
        assertNotNull(newAccessToken);
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithExpiredRefreshTokenIsUnauthorized() throws Exception {
        final var content = sendDtoAndGetMvcResult(post(AUTH_LOGIN), createValidUserLoginDto(),
                status().isOk()).getResponse().getContentAsString();
        final var refreshToken = objectMapper.readTree(content).get(REFRESH_TOKEN).textValue();
        TimeUnit.SECONDS.sleep(refreshTokenExpirationTime);
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header(REFRESH_TOKEN, BEARER_PREFIX + refreshToken), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getRefreshTokenStartExceptionMessage()));
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithInvalidRefreshTokenIsUnauthorized() throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header(REFRESH_TOKEN, BEARER_PREFIX + INVALID_TOKEN), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getRefreshTokenStartExceptionMessage()));
    }

    private String obtainToken(UserLoginDto loginDto) throws Exception {
        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_LOGIN), loginDto, status().isOk());
        return JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.access_token");
    }

    private HttpHeaders getAuthorizationHeader(String token) {
        final var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }

    private HttpHeaders getAuthorizationHeaderWithValidToken() throws Exception {
        return getAuthorizationHeader(BEARER_PREFIX + obtainToken(createValidUserLoginDto()));
    }

    private String getRefreshTokenValue() throws Exception {
        final var content = sendDtoAndGetMvcResult(post(AUTH_LOGIN), createValidUserLoginDto(),
                status().isOk()).getResponse().getContentAsString();
        return objectMapper.readTree(content).get(REFRESH_TOKEN).textValue();
    }

    private UserLoginDto createValidUserLoginDto() {
        return new UserLoginDto(VALID_USERNAME, VALID_PASSWORD);
    }

    private UserLoginDto createNotValidUserLoginDto() {
        return new UserLoginDto(NOT_VALID_USERNAME, NOT_VALID_PASSWORD);
    }

    private String getRefreshTokenStartExceptionMessage() {
        return getMessageSource("refresh.token.invalid").substring(0, 24);
    }
}
