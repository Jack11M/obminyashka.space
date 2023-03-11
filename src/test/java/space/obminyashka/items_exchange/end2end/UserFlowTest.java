package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getTestChildren;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class UserFlowTest extends BasicControllerTest {

    private static final String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I";
    private static final String NEW_USER_EMAIL = "test@test.com";
    private static final String USER_FIRST_NAME = "First";
    private static final String USER_LAST_NAME = "Last";
    private static final String ADMIN_USERNAME = "admin";

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
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    void getUserDto_shouldReturnUserDtoIfExists() throws Exception {
        sendUriAndGetResultAction(get(USER_MY_INFO), status().isOk())
                .andExpect(jsonPath("$.username").value(ADMIN_USERNAME));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/update.yml", orderBy = {"created", "name"}, ignoreCols = {"last_online_time", "updated", "email"})
    void updateUserInfo_shouldUpdateUserData() throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDto(), status().isAccepted());

        var responseContentAsString = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContentAsString.contains(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_INFO)));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
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
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    void getChildren_Success_ShouldReturnUsersChildren() throws Exception {
        sendUriAndGetResultAction(get(USER_CHILD), status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sex").value("MALE"))
                .andExpect(jsonPath("$[1].sex").value("FEMALE"));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/update.yml", orderBy = {"created", "name", "birth_date"}, ignoreCols = "id")
    void updateChild_Success_ShouldReturnHttpStatusOk() throws Exception {
        var validUpdatingChildDtoJson = getTestChildren(2018);

        sendDtoAndGetResultAction(put(USER_CHILD), validUpdatingChildDtoJson, status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml", orderBy = "created",
            ignoreCols = {"password", "email", "lastOnlineTime", "updated"})
    void updateUserPassword_WhenDataCorrect_Successfully() throws Exception {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isAccepted());

        String pwd = userService.findByUsernameOrEmail(ADMIN_USERNAME).map(User::getPassword).orElse("");

        assertTrue(bCryptPasswordEncoder.matches(userChangePasswordDto.getNewPassword(), pwd));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_PASSWORD)));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml", orderBy = "created",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void updateUserEmail_WhenDataIsCorrect_Successfully() throws Exception {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_VALID_EMAIL, NEW_VALID_EMAIL);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), userChangeEmailDto, status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_EMAIL)));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/delete_user_first_expected.yml", orderBy = "created",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void selfDeleteRequest_WhenDataCorrect_Successfully() throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(delete(USER_SERVICE_DELETE), userDeleteFlowDto, status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(
                getParametrizedMessageSource(ResponseMessagesHandler.PositiveMessage.DELETE_ACCOUNT, numberOfDaysToKeepDeletedUsers)));
    }

    @Test
    @WithMockUser(username = "deletedUser", roles = "SELF_REMOVING")
    @DataSet(value = {"database_init.yml", "user/deleted_user_init.yml"})
    @ExpectedDataSet(value = "user/deleted_user_restore_expected.yml", orderBy = {"created", "name"},
            ignoreCols = {"password", "lastOnlineTime", "updated", "status"})
    void makeAccountActiveAgain_WhenDataCorrect_Successfully() throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_RESTORE), userDeleteFlowDto, status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.PositiveMessage.ACCOUNT_ACTIVE_AGAIN)));
    }

    @Test
    @Commit
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/login_with_oauth2.yml", orderBy = {"created", "name"},
            ignoreCols = {"id", "password", "created", "updated", "last_online_time"})
    @WithMockUser(username = NEW_USER_EMAIL)
    void loginNewUserViaOauth2_shouldCreateValidUser() throws Exception {
        var oauth2User = createDefaultOidcUser();
        var user = userService.loginUserWithOAuth2(oauth2User);
        assertNotNull(user);
        assertEquals(true, user.getIsOauth2Login());

        sendUriAndGetResultAction(get(USER_MY_INFO), status().isOk())
                .andExpect(jsonPath("$.username").value(NEW_USER_EMAIL))
                .andExpect(jsonPath("$.email").value(NEW_USER_EMAIL))
                .andExpect(jsonPath("$.firstName").value(USER_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(USER_LAST_NAME));
    }

    private DefaultOidcUser createDefaultOidcUser() {
        var familyName = "family_name";
        var givenName = "given_name";
        var roleUser = "ROLE_USER";
        var groupsKey = "groups";
        var emailKey = "email";
        var subKey = "sub";

        var idToken = new OidcIdToken(
                ID_TOKEN,
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of(groupsKey, roleUser, subKey, 123)
        );

        final var userInfo = new OidcUserInfo(Map.of(emailKey, NEW_USER_EMAIL, givenName, USER_FIRST_NAME, familyName, USER_LAST_NAME));
        return new DefaultOidcUser(Collections.singletonList(new SimpleGrantedAuthority(roleUser)), idToken, userInfo);
    }

    @WithMockUser
    @DataSet("database_init.yml")
    @Test
    void updateUserAvatar_shouldSetNewImage_whenFormatAndContentValid() throws Exception {
        var jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Path.of("src/test/resources/image/test-image.jpeg")));

        final var mvcResult = mockMvc.perform(multipart(USER_SERVICE_CHANGE_AVATAR)
                        .file(jpeg)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isAccepted())
                .andReturn();

        final var avatarImage = new JSONObject(mvcResult.getResponse().getContentAsString()).getString("avatarImage");
        assertFalse(avatarImage.isBlank());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/remove_avatar.yml", orderBy = {"created", "name"},
            ignoreCols = {"id", "password", "created", "updated", "last_online_time"})
    void removeUserAvatar_whenAuthorized_shouldRemoveAvatar() throws Exception {
        sendUriAndGetMvcResult(delete(USER_SERVICE_CHANGE_AVATAR), status().isOk());
    }
}
