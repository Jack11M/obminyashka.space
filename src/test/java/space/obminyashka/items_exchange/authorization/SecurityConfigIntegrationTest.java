package space.obminyashka.items_exchange.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.test.web.servlet.MockMvc;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserLoginDto;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
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
    void createAdvertisementWithoutTokenIsUnauthorizedResponse() throws Exception {
        sendUriAndGetMvcResult(post(ADV), status().isUnauthorized());
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
    void postRequestWithNotValidJWTTokenIsUnauthorizedResponse() throws Exception {
        final var invalidToken = BEARER_PREFIX + obtainToken(createValidUserLoginDto()).replaceAll(".$", "");
        sendUriWithHeadersAndGetMvcResult(post(ADV), status().isUnauthorized(), getAuthorizationHeader(invalidToken));
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithAccessTokenAndEmptyBodyShouldReturnBadRequest() throws Exception {
        final var headers = getAuthorizationHeaderWithValidToken();
        final var mvcResult = sendUriWithHeadersAndGetMvcResult(multipart(ADV), status().isBadRequest(), headers);
        final var errorMessage = new JSONObject(mvcResult.getResponse().getContentAsString()).get("error");
        assertEquals("Required request part 'dto' is not present", errorMessage);
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestToRefresh_shouldReturnNewAccessToken() throws Exception {
        final var refreshToken = getRefreshTokenValue();
        TimeUnit.MILLISECONDS.sleep(accessJwtExpirationTime);
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header("refresh", BEARER_PREFIX + refreshToken), status().isOk());
        final var newAccessToken = objectMapper.readTree(mvcResult.getResponse().getContentAsString())
                .get(OAuth2ParameterNames.REFRESH_TOKEN).textValue();
        assertNotNull(newAccessToken);
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithExpiredRefreshTokenIsUnauthorized() throws Exception {
        final var content = sendDtoAndGetMvcResult(post(AUTH_LOGIN), createValidUserLoginDto(),
                status().isOk()).getResponse().getContentAsString();
        final var refreshToken = objectMapper.readTree(content).get(OAuth2ParameterNames.REFRESH_TOKEN).textValue();
        TimeUnit.SECONDS.sleep(refreshTokenExpirationTime);
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header("refresh", BEARER_PREFIX + refreshToken), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getRefreshTokenStartExceptionMessage()));
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithInvalidRefreshTokenIsUnauthorized() throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(post(AUTH_REFRESH_TOKEN)
                .header("refresh", BEARER_PREFIX + INVALID_TOKEN), status().isUnauthorized());
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
        return objectMapper.readTree(content).get(OAuth2ParameterNames.REFRESH_TOKEN).textValue();
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
