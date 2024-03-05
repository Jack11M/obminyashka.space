package space.obminyashka.items_exchange.end2end;

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
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.request.UserLoginRequest;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SecurityConfigFlowTest extends BasicControllerTest {
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "@kuIOIY*h986";
    private static final String NOT_VALID_USERNAME = "nimda";
    private static final String NOT_VALID_PASSWORD = "drowssap";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_TOKEN = "DefinitelyNotValidToken";

    @Value("${app.refresh.jwt.expiration.time.seconds}")
    private long refreshTokenExpirationTime;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public SecurityConfigFlowTest(MockMvc mockMvc) {
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
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_USERNAME_PASSWORD)));
    }

    @Test
    void createAdvertisementWithoutTokenIsUnauthorizedResponse() throws Exception {
        sendUriAndGetMvcResult(post(ADV), status().isUnauthorized());
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
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MissingServletRequestPartException.class)
                .hasMessage("Required part 'dto' is not present.");
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestToRefresh_shouldReturnNewAccessToken() throws Exception {
        final var refreshToken = getRefreshTokenValue();
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

    private String obtainToken(UserLoginRequest loginDto) throws Exception {
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

    private UserLoginRequest createValidUserLoginDto() {
        return new UserLoginRequest(VALID_USERNAME, VALID_PASSWORD);
    }

    private UserLoginRequest createNotValidUserLoginDto() {
        return new UserLoginRequest(NOT_VALID_USERNAME, NOT_VALID_PASSWORD);
    }

    private String getRefreshTokenStartExceptionMessage() {
        return getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_REFRESH_TOKEN).substring(0, 24);
    }
}
