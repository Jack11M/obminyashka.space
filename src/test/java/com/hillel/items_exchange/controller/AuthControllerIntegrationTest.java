package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.UserLoginDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.security.jwt.InvalidatedTokensHolder;
import com.hillel.items_exchange.util.AuthControllerIntegrationTestUtil;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class AuthControllerIntegrationTest extends AuthControllerIntegrationTestUtil {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InvalidatedTokensHolder invalidatedTokensHolder;

    @Test
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "auth/register_user.yml", ignoreCols = {"password", "created", "updated", "last_online_time"})
    void register_shouldCreateValidNewUserAndReturnCreated() throws Exception {
        UserRegistrationDto validUser = createUserRegistrationDto(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(validUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenDtoIsValid_shouldReturnSpecificSuccessMessage() throws Exception {
        UserRegistrationDto validUser = createUserRegistrationDto(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        MvcResult result = mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(validUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andReturn();

        String seekingResponse = getParametrizedMessageSource("user.created", validUser.getUsername());
        assertTrue(result.getResponse().getContentAsString().contains(seekingResponse));
    }

    @Test
    void register_whenUserRegistrationDtoIsEmpty_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(REGISTER_URL, "")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_whenUserRegistrationDtoIsNull_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(REGISTER_URL, (Object) null)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUsernameOrEmailExists_shouldReturnUnprocessableEntityAndThrowUnprocessableEntityException()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(VALID_USERNAME, EXISTENT_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(UndeclaredThrowableException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUsernameExists_shouldReturnSpecificErrorMessage()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(EXISTENT_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(getMessageSource("username.duplicate")));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenEmailExists_shouldReturnSpecificErrorMessage()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(VALID_USERNAME, EXISTENT_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(getMessageSource("email.duplicate")));
    }

    @Test
    @DataSet("database_init.yml")
    void register_whenDifferentConfirmPassword_shouldReturnBadRequestAndThrowBadRequestException() throws Exception {
        UserRegistrationDto invalidConfirmPasswordUser = createUserRegistrationDto(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, INVALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidConfirmPasswordUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(BadRequestException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenDifferentConfirmPassword_shouldReturnSpecificErrorMessage()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, INVALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String receivedMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(getMessageSource("different.passwords"), receivedMessage);
    }

    @Test
    void register_whenPasswordInvalid_shouldReturnBadRequest() throws Exception {
        UserRegistrationDto invalidPasswordUser = createUserRegistrationDto(VALID_USERNAME, VALID_EMAIL,
                INVALID_PASSWORD, INVALID_PASSWORD);
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidPasswordUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenPasswordInvalid_shouldReturnSpecificErrorMessage()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(VALID_USERNAME, VALID_EMAIL,
                INVALID_PASSWORD, INVALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String receivedMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertTrue(receivedMessage.contains(getMessageSource("invalid.password")));
    }

    @Test
    void register_whenEmailInvalid_shouldReturnBadRequest() throws Exception {
        UserRegistrationDto invalidEmailUser = createUserRegistrationDto(VALID_USERNAME, INVALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenEmailInvalid_shouldReturnSpecificErrorMessage()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(VALID_USERNAME, INVALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String receivedMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertTrue(receivedMessage.contains(getMessageSource("invalid.email")));
    }

    @Test
    void register_whenUsernameInvalid_shouldReturnBadRequest() throws Exception {
        UserRegistrationDto invalidNameUser = createUserRegistrationDto(INVALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);

        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidNameUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUsernameInvalid_shouldReturnSpecificErrorMessage()
            throws Exception {

        UserRegistrationDto existEmailUser = createUserRegistrationDto(INVALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_PASSWORD);
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(existEmailUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        String receivedMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertTrue(receivedMessage.contains(getMessageSource("invalid.username")));
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void login_Success_shouldReturnHttpOk() throws Exception {
        final String loginDto = asJsonString(UserLoginDto.builder()
                .usernameOrEmail(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .build());
        mockMvc.perform(post(LOGIN_URL)
                .content(loginDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'firstname':'firstname'}"))
                .andExpect(content().json("{'lastname':'lastname'}"))
                .andExpect(content().json("{'avatarImage':'dGVzdCBpbWFnZSBwbmc='}"));
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void logout_Success_ShouldReturnNoContent() throws Exception {
        final String token = obtainToken(createUserLoginDto());
        mockMvc.perform(post(LOGOUT_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void logout_Success_ShouldBeInvalidatedInInvalidatedTokensHolder() throws Exception {
        final String token = obtainToken(createUserLoginDto());
        mockMvc.perform(post(LOGOUT_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        assertTrue(invalidatedTokensHolder.isInvalidated(token));
    }

    @Test
    @DataSet(value = "auth/login.yml")
    void logout_Failure_ShouldThrowJwtExceptionAfterRequestWithInvalidToken() throws Exception{
        final String token = "DefinitelyNotValidToken";
        mockMvc.perform(post(LOGOUT_URL)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private String obtainToken(UserLoginDto loginDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                .content(asJsonString(loginDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }
}
