package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.ChildAddDto;
import com.hillel.items_exchange.dto.ChildUpdateDto;
import com.hillel.items_exchange.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.UserDtoCreatingUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String validCreatingChildDtoJson;
    private String validUpdatingChildDtoJson;
    private String notValidUpdatingChildDtoJson;
    private String badTotalAmountChildDtoJson;
    private String badAmountChildDtoJson;

    @BeforeEach
    void setUp() {
        validCreatingChildDtoJson = getJsonOfChildrenDto(0L, 0L, 2019);
        validUpdatingChildDtoJson = getJsonOfChildrenDto(1L, 2L, 2018);
        notValidUpdatingChildDtoJson = getJsonOfChildrenDto(1L, 999L, 2018);
        badTotalAmountChildDtoJson = getJsonOfChildrenDto(2010, 8);
        badAmountChildDtoJson = getJsonOfChildrenDto(2011, 11);
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void getUserDto_shouldReturnUserDtoIfExists() throws Exception {
        mockMvc.perform(get("/user/my-info")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void negativeTestReceivingInformationAboutAnotherUser() throws Exception {
        mockMvc.perform(get("/user/my-info")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update.yml", ignoreCols = {"last_online_time", "updated"})
    void updateUserInfo_shouldUpdateUserData() throws Exception {
        getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedEmailAndFNameApAndLNameMinusWithoutChildrenOrPhones(), status().isAccepted())
                .andDo(print())
                .andExpect(jsonPath("$.email").value(NEW_EMAIL))
                .andExpect(jsonPath("$.firstName").value(NEW_VALID_NAME_WITH_APOSTROPHE))
                .andExpect(jsonPath("$.lastName").value(NEW_VALID_NAME_WITH_HYPHEN_MINUS));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturn403WhenUsernameIsChanged() throws Exception {
        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedUsernameWithoutChildrenOrPhones(), status().isForbidden())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(
                getExceptionMessageSource("exception.illegal.field.change") + "Username"));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturn403WhenLastOnlineTimeIsChanged() throws Exception {
        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedLastOnlineTimeWithoutChildrenOrPhones(), status().isForbidden())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(
                getExceptionMessageSource("exception.illegal.field.change") + "LastOnlineTime"));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturn400WhenShortFirstName() throws Exception {
        getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithInvalidShortFNameWithoutChildrenOrPhones(), status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturn400WhenLastNameContainsTwoWords() throws Exception {
        getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithInvalidLNameWithoutChildrenOrPhones(), status().isBadRequest())
                .andReturn();
    }

    private ResultActions getResultActions(HttpMethod httpMethod, String path,
                                           UserDto dto, ResultMatcher matcher) throws Exception {
        MockHttpServletRequestBuilder builder = request(httpMethod, path)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto));
        return mockMvc.perform(builder).andExpect(matcher);
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void getChildren_Success_ShouldReturnUsersChildren() throws Exception {
        mockMvc.perform(get("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].sex").value("male"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].sex").value("female"));

    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "/children/create.yml", ignoreCols = {"birth_date", "sex"})
    void addChildren_Success_ShouldReturnHttpStatusOk() throws Exception {
        mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/delete.yml")
    void removeChild_Success_ShouldReturnHttpStatusOk() throws Exception {
        mockMvc.perform(delete("/user/child/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/update.yml")
    void updateChild_Success_ShouldReturnHttpStatusOk() throws Exception {
        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void updateChild_NotValidDto_ShouldThrowIllegalIdentifierException() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notValidUpdatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Not all children from dto present in"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void addChildren_BadTotalAmount_ShouldThrowEntityAmountException() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badTotalAmountChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("You can't add more than 10 children"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void addChildren_BadAmount_ShouldThrowConstraintViolationException() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badAmountChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Validation exception"));
    }

    private String getJsonOfChildrenDto(long maleId, long femaleId, int year) {
        return asJsonString(List.of(
                getChildDto(maleId, LocalDate.of(year, 3, 3), "male"),
                getChildDto(femaleId, LocalDate.of(year, 4, 4), "female")
        ));
    }

    private String getJsonOfChildrenDto(int year, int quantity) {
        List<ChildAddDto> childAddDtoList = new ArrayList<>();
        for(int i = 0; i < quantity; i++) {
            childAddDtoList.add(getChildDto(
                    LocalDate.of(year, (int)((Math.random() * 12) + 1), (int)((Math.random() * 28) + 1)), "male"));
        }
        return asJsonString(childAddDtoList);
    }

    private ChildUpdateDto getChildDto(long id, LocalDate birthDate, String sex) {
        return ChildUpdateDto.builder()
                .id(id)
                .birthDate(birthDate)
                .sex(sex)
                .build();
    }

    private ChildAddDto getChildDto(LocalDate birthDate, String sex) {
        return ChildAddDto.builder()
                .birthDate(birthDate)
                .sex(sex)
                .build();
    }
}