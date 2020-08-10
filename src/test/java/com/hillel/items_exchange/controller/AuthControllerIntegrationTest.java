package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.UserLoginDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.util.AuthControllerIntegrationTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
public class AuthControllerIntegrationTest extends AuthControllerIntegrationTestUtil {

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(UnprocessableEntityException.class)));
    }

    @Test
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
                .andExpect(status().isOk());
    }
}
