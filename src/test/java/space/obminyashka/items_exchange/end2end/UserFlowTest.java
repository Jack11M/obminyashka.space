package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.controller.request.ChangeEmailRequest;
import space.obminyashka.items_exchange.controller.request.ChangePasswordRequest;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.exception.not_found.EntityIdNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ExceptionMessage.*;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.PositiveMessage.CHANGED_USER_PASSWORD;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.SAME_PASSWORDS;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserFlowTest extends BasicControllerTest {

    private static final String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I";
    private static final String NEW_USER_EMAIL = "test@test.com";
    private static final String USER_FIRST_NAME = "First";
    private static final String USER_LAST_NAME = "Last";
    private static final String ADMIN_USERNAME = "admin";
    private static final String DOMAIN_URL = "https://obminyashka.space";
    private static final String INVALID_ADV_ID = "61731cc8-8104-49f0-b2c3-5a52e576ab28";
    private static final String VALID_ADV_ID = "65e3ee49-5927-40be-aafd-0461ce45f295";
    private static final String SECOND_VALID_ADV_ID = "4bd38c87-0f00-4375-bd8f-cd853f0eb9bd";

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private SendGrid sendGrid;

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
    @ExpectedDataSet(value = "user/update.yml", orderBy = {"created", "name"}, ignoreCols = {"last_online_time", "updated", "email", "id"})
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
    void getFavoriteAdvertisements_shouldReturnPage_WhenPageAndSizeDefault() throws Exception {
        sendUriAndGetResultAction(get(USER_MY_FAVORITE), status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value("topic"))
                .andExpect(jsonPath("$.content[1].title").value("Blouses"));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    void getFavoriteAdvertisements_shouldReturnPage_WhenHaveSpecialPageAndSize() throws Exception {
        sendUriAndGetResultAction(get(USER_MY_FAVORITE)
                .queryParam("page", "1")
                .queryParam("size", "1"), status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Blouses"));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/addNewFavorite.yml")
    void addFavoriteAdvertisement_shouldAddFavoriteAdvertisement_whenAdvertisementIsNotFavorite() throws Exception {
        sendUriAndGetResultAction(post(USER_MY_FAVORITE_ADV, SECOND_VALID_ADV_ID), status().isAccepted());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/addFavorite.yml")
    void addFavoriteAdvertisement_shouldReturnException_whenAdvertisementIsNotFound() throws Exception {
        var resultActions = sendUriAndGetResultAction(post(USER_MY_FAVORITE_ADV, INVALID_ADV_ID), status().isNotFound());

        Assertions.assertThat(resultActions.andReturn().getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class)
                .hasMessage(getParametrizedMessageSource(ADVERTISEMENT_NOT_EXISTED_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/deleteFavorite.yml")
    void deleteFavoriteAdvertisement_shouldDeleteFavoriteAdvertisement_whenAdvertisementIsFavorite() throws Exception {
        sendUriAndGetResultAction(delete(USER_MY_FAVORITE_ADV, VALID_ADV_ID), status().isNoContent());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    void deleteFavoriteAdvertisement_shouldReturnException_whenAdvertisementIsNotFavorite() throws Exception {
        var resultActions = sendUriAndGetResultAction(delete(USER_MY_FAVORITE_ADV, INVALID_ADV_ID),
                status().isNotFound());

        Assertions.assertThat(resultActions.andReturn().getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class)
                .hasMessage(getParametrizedMessageSource(FAVORITE_ADVERTISEMENT_NOT_FOUND, INVALID_ADV_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml", orderBy = "created",
            ignoreCols = {"password", "email", "lastOnlineTime", "updated"})
    void updateUserPassword_shouldGetResponseOK_whenDataValid()
            throws Exception {
        var changePasswordRequest = new ChangePasswordRequest(NEW_PASSWORD, NEW_PASSWORD);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), changePasswordRequest, status().isAccepted());
        String pwd = userService.findByUsernameOrEmail(ADMIN_USERNAME).map(User::getPassword).orElse("");

        assertTrue(bCryptPasswordEncoder.matches(NEW_PASSWORD, pwd));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(CHANGED_USER_PASSWORD)));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml", orderBy = "created",
            ignoreCols = {"password", "email", "lastOnlineTime", "updated"})
    void updateUserPassword_shouldReturnException_whenPasswordsSame() throws Exception {
        var changePasswordRequest = new ChangePasswordRequest(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), changePasswordRequest, status().isConflict());
        String pwd = userService.findByUsernameOrEmail(ADMIN_USERNAME).map(User::getPassword).orElse("");

        assertTrue(bCryptPasswordEncoder.matches(CORRECT_OLD_PASSWORD, pwd));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(SAME_PASSWORDS)));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml", orderBy = "created",
            ignoreCols = {"password", "email", "lastOnlineTime", "updated"})
    void updateUserEmail_shouldGetResponse() throws Exception {
        when(sendGrid.api(any())).thenReturn(new Response(200, null, null));
        var changeEmailRequest = new ChangeEmailRequest(OLD_ADMIN_VALID_EMAIL);
        var httpTemplate = put(USER_SERVICE_CHANGE_EMAIL).header(HttpHeaders.HOST, DOMAIN_URL);

        MvcResult mvcResult = sendDtoAndGetMvcResult(httpTemplate, changeEmailRequest, status().isConflict());
        String userEmail = userService.findByUsernameOrEmail(ADMIN_USERNAME).map(User::getEmail).orElse("");

        assertEquals(userEmail, changeEmailRequest.email());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_OLD)));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/changing_password_or_email_expected.yml", orderBy = "created",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void updateUserEmail_whenDataIsCorrect_successfully() throws Exception {
        when(sendGrid.api(any())).thenReturn(new Response(200, null, null));
        var changeEmailRequest = new ChangeEmailRequest(NEW_VALID_EMAIL);
        var httpTemplate = put(USER_SERVICE_CHANGE_EMAIL).header(HttpHeaders.HOST, DOMAIN_URL);

        MvcResult mvcResult = sendDtoAndGetMvcResult(httpTemplate, changeEmailRequest, status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_EMAIL)));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME)
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "user/delete_user_first_expected.yml", orderBy = "created",
            ignoreCols = {"password", "lastOnlineTime", "updated"})
    void selfDeleteRequest_whenDataCorrect_successfully() throws Exception {
        MvcResult mvcResult = sendUriAndGetMvcResult(delete(USER_SERVICE_DELETE), status().isAccepted());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(
                getParametrizedMessageSource(ResponseMessagesHandler.PositiveMessage.DELETE_ACCOUNT, numberOfDaysToKeepDeletedUsers)));
    }

    @Test
    @WithMockUser(username = "deletedUser", roles = "SELF_REMOVING")
    @DataSet(value = {"database_init.yml", "user/deleted_user_init.yml"})
    @ExpectedDataSet(value = "user/deleted_user_restore_expected.yml", orderBy = {"created", "name"},
            ignoreCols = {"password", "lastOnlineTime", "updated", "status"})
    void makeAccountActiveAgain_whenDataCorrect_successfully() throws Exception {
        MvcResult mvcResult = sendUriAndGetMvcResult(put(USER_SERVICE_RESTORE), status().isAccepted());

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
        assertEquals(true, user.getOauth2Login());
        assertEquals(true, user.isValidatedEmail());

        sendUriAndGetResultAction(get(USER_MY_INFO), status().isOk())
                .andExpect(jsonPath("$.username").value(NEW_USER_EMAIL))
                .andExpect(jsonPath("$.email").value(NEW_USER_EMAIL))
                .andExpect(jsonPath("$.firstName").value(USER_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(USER_LAST_NAME));
    }

    private DefaultOidcUser createDefaultOidcUser() {
        var roleUser = "ROLE_USER";

        final var now = Instant.now();
        var idToken = new OidcIdToken(
                ID_TOKEN,
                now,
                now.plusSeconds(60),
                Map.of("groups", roleUser, "sub", 123));

        final var userInfo = new OidcUserInfo(Map.of(
                "email", NEW_USER_EMAIL,
                "given_name", USER_FIRST_NAME,
                "family_name", USER_LAST_NAME,
                "email_verified", true));

        return new DefaultOidcUser(Collections.singletonList(new SimpleGrantedAuthority(roleUser)), idToken, userInfo);
    }

    @Test
    @WithMockUser
    @DataSet("database_init.yml")
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

    @Test
    @DataSet(value = {"database_init.yml", "user/delete_self-removing-users_init.yml"})
    @ExpectedDataSet(
            value = "database_init.yml",
            orderBy = {"created", "name", "birth_date"},
            ignoreCols = {"id", "password", "created", "updated", "last_online_time", "resource"})
    void permanentlyDeleteUsers_whenTimeComes_shouldRemoveUsers() {
        assertEquals(4, userRepository.count());

        userService.permanentlyDeleteUsers();

        assertEquals(2, userRepository.count());
    }
}
