package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getTestChildren;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class UserFlowTest extends BasicControllerTest {

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    public UserFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void getUserDto_shouldReturnUserDtoIfExists() throws Exception {
        sendUriAndGetResultAction(get(USER_MY_INFO), status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update.yml", ignoreCols = {"last_online_time", "updated", "email"})
    void updateUserInfo_shouldUpdateUserData() throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDto(), status().isAccepted());

        var responseContentAsString = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContentAsString.contains(getMessageSource("changed.user.info")));

    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void getCreatedAdvertisements_shouldReturnAll() throws Exception {
        sendUriAndGetResultAction(get(USER_MY_ADV), status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title").value("topic"))
                .andExpect(jsonPath("$[1].title").value("Blouses"))
                .andExpect(jsonPath("$[2].title").value("Dresses"))
                .andExpect(jsonPath("$[3].title").value("Skirts"))
                .andExpect(jsonPath("$[4].title").value("Pajamas"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void getChildren_Success_ShouldReturnUsersChildren() throws Exception {
        sendUriAndGetResultAction(get(USER_CHILD), status().isOk())
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
        var validCreatingChildDtoJson = getTestChildren(0L, 0L, 2019);

        sendDtoAndGetResultAction(post(USER_CHILD), validCreatingChildDtoJson, status().isOk())
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
        sendUriAndGetMvcResult(delete(USER_CHILD + "/1"), status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/update.yml")
    void updateChild_Success_ShouldReturnHttpStatusOk() throws Exception {
        var validUpdatingChildDtoJson = getTestChildren(1L, 2L, 2018);

        sendDtoAndGetResultAction(put(USER_CHILD), validUpdatingChildDtoJson, status().isOk())
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

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isAccepted());

        String pwd = userService.findByUsernameOrEmail("admin").map(User::getPassword).orElse("");

        assertTrue(bCryptPasswordEncoder.matches(userChangePasswordDto.getNewPassword(), pwd));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("changed.user.password")));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void updateUserEmail_WhenDataIsCorrect_Successfully() throws Exception {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_VALID_EMAIL, NEW_VALID_EMAIL);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), userChangeEmailDto, status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("changed.user.email")));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/delete_user_first_expected.yml",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void selfDeleteRequest_WhenDataCorrect_Successfully() throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(delete(USER_SERVICE_DELETE), userDeleteFlowDto, status().isAccepted());

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

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_RESTORE), userDeleteFlowDto, status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("account.made.active.again")));
    }
}
