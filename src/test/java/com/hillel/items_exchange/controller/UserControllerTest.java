package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static com.hillel.items_exchange.util.UserDtoCreatingUtil.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private String notValidCreatingChildDtoJson;
    private String validUpdatingChildDtoJson;
    private String notValidUpdatingChildDtoJson;

    @BeforeEach
    void setUp() {
        validCreatingChildDtoJson = asJsonString(List.of(
                childDtoBuilder(0L, LocalDate.of(2019, 3, 3), "male"),
                childDtoBuilder(0L, LocalDate.of(2019, 4, 4), "female")));
        notValidCreatingChildDtoJson = asJsonString(List.of(
                childDtoBuilder(111L, LocalDate.of(2019, 3, 3), "male"),
                childDtoBuilder(222L, LocalDate.of(2019, 4, 4), "female")));
        validUpdatingChildDtoJson = asJsonString(List.of(
                childDtoBuilder(1L, LocalDate.of(2018, 3, 3), "male"),
                childDtoBuilder(2L, LocalDate.of(2018, 4, 4), "female")));
        notValidUpdatingChildDtoJson = asJsonString(List.of(
                childDtoBuilder(1L, LocalDate.of(2018, 3, 3), "male"),
                childDtoBuilder(9999L, LocalDate.of(2018, 4, 4), "female")));
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
                .andExpect(status().isConflict())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(AccessDeniedException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update.yml", ignoreCols = {"last_online_time", "updated"})
    void updateUserInfo_shouldUpdateUserData() throws Exception {
        getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedEmailAndFirstNameApostrAndLastNameMinus(), status().isAccepted())
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
                createUserDtoForUpdatingWithChangedUsername(), status().isForbidden())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("You are unable to change your: username"));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldBeThrownAccessDeniedExceptionWhenNotUserId() throws Exception {
        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithNotUserId(), status().isConflict())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("You are not allowed to perform this action"));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturn400WhenShortFirstName() throws Exception {
        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithInvalidShortFirstName(), status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateUserInfo_shouldReturn400WhenLastNameContainsTwoWords() throws Exception {
        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithInvalidLastName(), status().isBadRequest())
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
    void addChild_NotValidDto_ShouldThrowIllegalIdentifierException() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notValidCreatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Zero ID is expected"));
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

    private ChildDto childDtoBuilder(long id, LocalDate birthDate, String sex){
        return ChildDto.builder().id(id).birthDate(birthDate).sex(sex).build();
    }
}