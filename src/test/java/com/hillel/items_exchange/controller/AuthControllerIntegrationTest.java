package com.hillel.items_exchange.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.util.AuthControllerIntegrationTestUtil;
import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
public class AuthControllerIntegrationTest extends AuthControllerIntegrationTestUtil {

    private final MockMvc mockMvc;

    @Autowired
    public AuthControllerIntegrationTest(MockMvc mockMvc) {
        super();
        this.mockMvc = mockMvc;
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_shouldCreateNewUserAndReturnCreated() throws Exception {
        UserRegistrationDto validUserRegistrationDto = createValidUserRegistrationDto();
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(validUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUserRegistrationDtoIsEmpty_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(post(REGISTER_URL, "")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUserRegistrationDtoIsNull_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(post(REGISTER_URL, (Object) null)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUserEmailExists_shouldReturnUnprocessableEntityAndThrowUnprocessableEntityException()
            throws Exception {

        UserRegistrationDto invalidUserRegistrationDto = createUserRegistrationDtoWithExistentEmail();
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(UnprocessableEntityException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUsernameExists_shouldReturnUnprocessableEntityAndThrowUnprocessableEntityException()
            throws Exception {

        UserRegistrationDto invalidUserRegistrationDto = createUserRegistrationDtoWithExistentUsername();
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(UnprocessableEntityException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenDifferentConfirmPassword_shouldReturnBadRequestAndThrowBadRequestException()
            throws Exception {

        UserRegistrationDto invalidUserRegistrationDto = createUserRegistrationDtoWithDifferentPasswords();
        MvcResult result = this.mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidUserRegistrationDto))
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
    void register_whenPasswordInvalid_shouldReturnReturnNotFound()
            throws Exception {

        UserRegistrationDto invalidUserRegistrationDto = createUserRegistrationDtoWithInvalidPassword();
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenEmailInvalid_shouldReturnNotFound()
            throws Exception {

        UserRegistrationDto invalidUserRegistrationDto = createUserRegistrationDtoWithInvalidEmail();
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void register_whenUsernameInvalid_shouldReturnNotFound()
            throws Exception {

        UserRegistrationDto invalidUserRegistrationDto = createUserRegistrationDtoWithInvalidUsername();
        mockMvc.perform(post(REGISTER_URL)
                .content(asJsonString(invalidUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
