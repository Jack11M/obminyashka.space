package space.obminyashka.items_exchange.security;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dto.AdvertisementDto;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class SecurityConfigIntegrationTest {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "@kuIOIY*h986";

    private static final String NOT_VALID_USERNAME = "nimda";
    private static final String NOT_VALID_PASSWORD = "drowssap";

    @Value("${app.jwt.expiration.time.ms}")
    private Long jwtTimeExpired;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    private MockMvc mockMvc;
    private UserLoginDto validLoginDto;
    private UserLoginDto notValidLoginDto;
    private AdvertisementModificationDto nonExistDto;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        createValidUserLoginDto();
        createNotValidUserLoginDto();
        nonExistDto = createNonExistAdvertisementModificationDto();
    }

    @Test
    @DataSet("database_init.yml")
    void loginWithValidUserIsOk() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .content(asJsonString(validLoginDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet("database_init.yml")
    void loginWithNotValidUserGetsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .content(asJsonString(notValidLoginDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAdvertisementWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/adv"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DataSet("database_init.yml")
    void createAdvertisementWithValidTokenWithoutAdvertisementDtoIsBadRequest() throws Exception {
        final String validToken = "Bearer " + obtainToken(validLoginDto);

        mockMvc.perform(post("/api/v1/adv")
                .header("Authorization", validToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/create.yml", ignoreCols = {"created", "updated"})
    void createAdvertisementWithValidTokenAndValidAdvertisementDtoIsOk() throws Exception {
        final String validToken = "Bearer " + obtainToken(validLoginDto);

        mockMvc.perform(post("/api/v1/adv")
                .header("Authorization", validToken)
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithJWTTokenWithoutBearerPrefixIsUnauthorizedAndBearerIsAbsent() throws Exception {
        final String tokenWithoutBearerPrefix = obtainToken(validLoginDto);

        String errorMessage = mockMvc.perform(post("/api/v1/adv")
                .header("Authorization", tokenWithoutBearerPrefix))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(getMessageSource("token.not.start.with.bearer"), errorMessage.trim());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithNotValidJWTTokenIsUnauthorizedAndBadTokenSignature() throws Exception {
        final String invalidToken = "Bearer " + obtainToken(validLoginDto).replaceAll(".$", "");

        String errorMessage = mockMvc.perform(post("/api/v1/adv")
                .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(getMessageSource("token.signature.not.valid"), errorMessage.trim());
    }

    @Test
    @DataSet("database_init.yml")
    void postRequestWithExpiredJwtTokenIsUnauthorizedAndTokenIsExpired() throws Exception {
        final String validToken = "Bearer " + obtainToken(validLoginDto);
        TimeUnit.MILLISECONDS.sleep(jwtTimeExpired);

        String errorMessage = mockMvc.perform(post("/api/v1/adv")
                .header("Authorization", validToken))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(Objects.requireNonNull(errorMessage).startsWith(getMessageSource("token.expired")));
    }

    private void createValidUserLoginDto() {
        validLoginDto = new UserLoginDto(VALID_USERNAME, VALID_PASSWORD);
    }

    private void createNotValidUserLoginDto() {
        notValidLoginDto = new UserLoginDto(NOT_VALID_USERNAME, NOT_VALID_PASSWORD);
    }

    private String extractToken(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    private String obtainToken(UserLoginDto loginDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .content(asJsonString(loginDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        return extractToken(result);
    }
}
