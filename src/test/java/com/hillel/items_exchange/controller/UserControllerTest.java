package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.PhoneDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.security.jwt.JwtUser;
import com.hillel.items_exchange.util.MockSpringSecurityFilter;
import com.hillel.items_exchange.util.UserDtoUpdatingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class UserControllerTest {
    private final static JwtUser JWT_USER = new JwtUser(1L, "admin", "super", "admin",
            "admin@gmail.com", "$2a$10$mdTVPX.45ozJhwrHXTi/0Ozn8sihqL.U1vtzQN0BGBcIgKeYY4Dz6",
            null, true, null);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void getUserDto_shouldReturnUserDtoIfExists() throws Exception {
        mockMvc.perform(get("/user/info/{id}", 1L)
                .content(asJsonString("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void negativeTestReceivingInformationAboutAnotherUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/info/{id}", 555555L)
                .content(asJsonString("admin"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(AccessDeniedException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update.yml", ignoreCols = {"last_online_time", "updated"})
    void updateUserInfo_shouldUpdateUserData() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.setEmail("new.admin@gmail.com");
        validUserDto.setFirstName("newFirstName");
        validUserDto.setLastName("newLastName");

        getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.email").value("new.admin@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("newFirstName"))
                .andExpect(jsonPath("$.lastName").value("newLastName"));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update_child.yml", ignoreCols = {"last_online_time", "updated"})
    void updateUserInfo_shouldUpdateUserChildren() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.getChildren().add(
                new ChildDto(0, "female", LocalDate.of(2020, 1, 1)));

        getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.children", hasSize(2)))
                .andExpect(jsonPath("$.children[1].sex").value("female"));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturnBadRequestIfInvalidChildBirthDate() throws Exception {
        UserDto invalidUserDto = UserDtoUpdatingUtil.getInvalidUserDtoWithInvalidChildBirthDate();

        getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, invalidUserDto, status().isBadRequest()).andReturn();
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void putUserDto_shouldBeThrownUnprocessableEntityException() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.getChildren().add(new ChildDto(5L, "male", LocalDate.now()));

        MvcResult result = getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isUnprocessableEntity()).andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(UnprocessableEntityException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update_phone.yml", ignoreCols = {"last_online_time", "updated"})
    void updateUserInfo_shouldUpdateUserPhones() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.getPhones().get(0).setPhoneNumber("+1 (234) 111-11-11");
        validUserDto.getPhones().add(new PhoneDto(0, Boolean.FALSE, "+38.050.222.22.22", Boolean.FALSE));
        validUserDto.getPhones().add(new PhoneDto(0, Boolean.FALSE, "+38-050-222-22-22", Boolean.FALSE));
        validUserDto.getPhones().add(new PhoneDto(0, Boolean.FALSE, "+38 050 222 22 22", Boolean.FALSE));
        validUserDto.getPhones().add(new PhoneDto(0, Boolean.FALSE, "+380502222222", Boolean.FALSE));
        validUserDto.getPhones().add(new PhoneDto(0, Boolean.FALSE, "38(050)2222222", Boolean.FALSE));

        getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.phones", hasSize(6)))
                .andExpect(jsonPath("$.phones[0].phoneNumber").value("12341111111"))
                .andExpect(jsonPath("$.phones[1].phoneNumber").value("380502222222"));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturnBadRequestIfInvalidPhoneNumber() throws Exception {
        UserDto invalidUserDto = UserDtoUpdatingUtil.getInvalidUserDtoWithInvalidPhoneNumber();

        getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, invalidUserDto, status().isBadRequest()).andReturn();
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void putUserDto_shouldBeThrownUnprocessableEntityExceptionIfPhoneHasWrongUserId() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.getPhones().add(new PhoneDto(5L, Boolean.FALSE, "+1 234 567 89 01", Boolean.FALSE));

        MvcResult result = getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isUnprocessableEntity()).andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(UnprocessableEntityException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldBeThrownIllegalOperationExceptionIfChangedPassword() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.setPassword("new password");

        MvcResult result = getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isMethodNotAllowed()).andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(IllegalOperationException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void putUserDto_shouldBeThrownIllegalOperationExceptionIfChangedUsername() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();
        validUserDto.setUsername("newUsername123");

        MvcResult result = getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, validUserDto, status().isMethodNotAllowed()).andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(IllegalOperationException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void putUserDto_shouldBeThrownAccessDeniedException() throws Exception {
        UserDto validUserDto = UserDtoUpdatingUtil.getValidUserDto();

        MvcResult result = getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 100L, validUserDto, status().isForbidden()).andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(AccessDeniedException.class)));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void putUserDto_shouldBeThrownMethodArgumentNotValidExceptionIfInvalidPhoneNumber() throws Exception {
        UserDto invalidUserDto = UserDtoUpdatingUtil.getInvalidUserDtoWithInvalidPhoneNumber();

        MvcResult result = getResultActionsWithAuthenticationPrincipal(
                HttpMethod.PUT, "/user/info/{id}", 1L, invalidUserDto, status().isBadRequest()).andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentNotValidException.class)));
    }

    private ResultActions getResultActionsWithAuthenticationPrincipal(HttpMethod httpMethod, String path, long uriVars,
                                                                      UserDto dto, ResultMatcher matcher) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        MockHttpServletRequestBuilder builder = request(httpMethod, path, uriVars)
                .principal(new UsernamePasswordAuthenticationToken(JWT_USER, null))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto));
        return mockMvc.perform(builder).andExpect(matcher);
    }
}