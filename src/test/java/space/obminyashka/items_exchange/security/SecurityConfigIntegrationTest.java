package space.obminyashka.items_exchange.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.security.jwt.refresh.RefreshTokenRequest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto;
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
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String ACCESS_TOKEN = "accessToken";
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
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
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
    @ExpectedDataSet(value = "advertisement/create.yml", ignoreCols = {"created", "updated"})
    void createAdvertisementWithValidTokenAndValidAdvertisementDtoIsOk() throws Exception {
        sendDtoWithHeadersAndGetResultAction(post(ADV), createNonExistAdvertisementModificationDto(), status().isCreated(),
                getAuthorizationHeaderWithValidToken())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithJWTTokenWithoutBearerPrefixIsUnauthorizedAndBearerIsAbsent() throws Exception {
        final String tokenWithoutBearerPrefix = obtainToken(createValidUserLoginDto());
        final var mvcResult = sendUriWithHeadersAndGetMvcResult(post(ADV), status().isUnauthorized(),
                getAuthorizationHeader(tokenWithoutBearerPrefix));
        assertEquals(getMessageSource("token.not.start.with.bearer"), mvcResult.getResponse().getContentAsString().trim());
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
    void postRequestToRefreshJwtIsOkAndCreateAdvWithNewAccessJwt() throws Exception {
        final var refreshToken = getRefreshTokenValue();
        TimeUnit.MILLISECONDS.sleep(accessJwtExpirationTime);
        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_REFRESH_TOKEN),
                new RefreshTokenRequest(refreshToken), status().isOk());
        final var newAccessToken = objectMapper.readTree(mvcResult.getResponse().getContentAsString())
                .get(ACCESS_TOKEN).textValue();
        sendDtoWithHeadersAndGetResultAction(post(ADV), createNonExistAdvertisementModificationDto(), status().isCreated(),
                getAuthorizationHeader(BEARER_PREFIX + newAccessToken))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithExpiredRefreshTokenIsUnauthorized() throws Exception {
        final var content = sendDtoAndGetMvcResult(post(AUTH_LOGIN), createValidUserLoginDto(),
                status().isOk()).getResponse().getContentAsString();
        final var refreshToken = objectMapper.readTree(content).get(REFRESH_TOKEN).textValue();
        TimeUnit.SECONDS.sleep(refreshTokenExpirationTime);
        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_REFRESH_TOKEN),
                new RefreshTokenRequest(refreshToken), status().isUnauthorized());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(
                getRefreshTokenStartExceptionMessage("refresh.token.expired")));
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithInvalidRefreshTokenIsNotFound() throws Exception {
        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_REFRESH_TOKEN),
                new RefreshTokenRequest(INVALID_TOKEN), status().isNotFound());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(
                getRefreshTokenStartExceptionMessage("refresh.token.not-found")));
    }

    private String obtainToken(UserLoginDto loginDto) throws Exception {
        final var mvcResult = sendDtoAndGetMvcResult(post(AUTH_LOGIN), loginDto, status().isOk());
        return JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.accessToken");
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

    private String getRefreshTokenStartExceptionMessage(String messageSource) {
        return getMessageSource(messageSource).substring(0, 13);
    }
}
