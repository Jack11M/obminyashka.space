package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.dto.UserWithoutChildrenDto;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getJsonOfChildrenDto;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class UserFlowTest {

    public static final String USER_INFO = "/user/info";
    public static final String PATH_USER_CHANGE_PASSWORD = "/user/service/pass";
    public static final String PATH_USER_CHANGE_EMAIL = "/user/service/email";
    public static final String USER_SERVICE_DELETE = "/user/service/delete";
    public static final String USER_SERVICE_RESTORE = "/user/service/restore";

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void getUserDto_shouldReturnUserDtoIfExists() throws Exception {
        mockMvc.perform(get("/user/my-info")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update.yml", ignoreCols = {"last_online_time", "email", "updated"})
    void updateUserInfo_Success_shouldReturnHttpStatusAccept() throws Exception {
        UserWithoutChildrenDto dto = createValidUserWithoutChildrenDtoForUpdating();

        getResultActions(HttpMethod.PUT, USER_INFO, dto, status().isAccepted())
                .andExpect(jsonPath("$.firstName").value(NEW_VALID_NAME))
                .andExpect(jsonPath("$.lastName").value(NEW_VALID_NAME_WITH_HYPHEN_MINUS))
                .andExpect(jsonPath("$.phones", hasSize(NEW_VALID_PHONES.size())));
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
                .andExpect(jsonPath("$[0].sex").value("MALE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].sex").value("FEMALE"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "/children/create.yml", ignoreCols = {"birth_date", "sex"})
    void addChildren_Success_ShouldReturnHttpStatusOk() throws Exception {
        String validCreatingChildDtoJson = getJsonOfChildrenDto(0L, 0L, 2019);

        mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("3"))
                .andExpect(jsonPath("$[0].birthDate").value("2019-03-03"))
                .andExpect(jsonPath("$[0].sex").value("MALE"))
                .andExpect(jsonPath("$[1].id").value("4"))
                .andExpect(jsonPath("$[1].birthDate").value("2019-04-04"))
                .andExpect(jsonPath("$[1].sex").value("FEMALE"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/delete.yml")
    void removeChild_Success_ShouldReturnHttpStatusOk() throws Exception {
        mockMvc.perform(delete("/user/child/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/update.yml")
    void updateChild_Success_ShouldReturnHttpStatusOk() throws Exception {
        String validUpdatingChildDtoJson = getJsonOfChildrenDto(1L, 2L, 2018);

        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUpdatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].birthDate").value("2018-03-03"))
                .andExpect(jsonPath("$[0].sex").value("MALE"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].birthDate").value("2018-04-04"))
                .andExpect(jsonPath("$[1].sex").value("FEMALE"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml",
            ignoreCols = {"password", "email", "lastOnlineTime", "updated"})
    void updateUserPassword_WhenDataCorrect_Successfully() throws Exception {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_PASSWORD, userChangePasswordDto,
                status().isAccepted())
                .andDo(print())
                .andReturn();

        User user = userService.findByUsernameOrEmail("admin").orElseThrow();

        assertTrue(bCryptPasswordEncoder.matches(userChangePasswordDto.getNewPassword(), user.getPassword()));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("changed.user.password")));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void updateUserEmail_WhenDataIsCorrect_Successfully() throws Exception {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_VALID_EMAIL, NEW_VALID_EMAIL);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_EMAIL, userChangeEmailDto,
                status().isAccepted())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("changed.user.email")));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/delete_user_first_expected.yml",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void selfDeleteRequest_WhenDataCorrect_Successfully() throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.DELETE, USER_SERVICE_DELETE, userDeleteFlowDto,
                status().isAccepted())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(
                getParametrizedMessageSource("account.self.delete.request", numberOfDaysToKeepDeletedUsers)));
    }

    @Test
    @WithMockUser(username = "deletedUser")
    @DataSet(value = {"database_init.yml", "user/deleted_user_init.yml"})
    @ExpectedDataSet(value = {"database_init.yml", "user/deleted_user_restore_expected.yml"},
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void makeAccountActiveAgain_WhenDataCorrect_Successfully() throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, USER_SERVICE_RESTORE, userDeleteFlowDto,
                status().isAccepted())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("account.made.active.again")));
    }

    private <T> ResultActions getResultActions(HttpMethod httpMethod, String path,
                                               T dto, ResultMatcher matcher) throws Exception {
        MockHttpServletRequestBuilder builder = request(httpMethod, path)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto));
        return mockMvc.perform(builder).andExpect(matcher);
    }
}
