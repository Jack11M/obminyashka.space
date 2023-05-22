package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceIntegrationTest {

    public static final String CORRECT_OLD_PASSWORD = "123456xX";
    public static final String NEW_USER_EMAIL = "user@mail.com";
    private static final String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I";
    private static final String USER_FIRST_NAME = "First";
    private static final String USER_LAST_NAME = "Last";

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleService roleService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> oauth2UserArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> usernameArgumentCaptor;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;
    private User userWithOldPassword;

    @BeforeEach
    void setUp() {
        userWithOldPassword = createUserWithOldPassword();
    }

    @Test
    void testIsUserPasswordMatches_WhenDataCorrect_Successfully() {
        userService.isUserPasswordMatches(userWithOldPassword.getUsername(), CORRECT_OLD_PASSWORD);

        assertTrue(bCryptPasswordEncoder.matches(CORRECT_OLD_PASSWORD, userWithOldPassword.getPassword()));
        verify(userRepository).getUserPasswordByUsername(userWithOldPassword.getUsername());
    }

    @Test
    void testSelfDeleteRequest_WhenDataCorrect_Successfully() {
        userService.selfDeleteRequest(NEW_USER_EMAIL);

        verify(userRepository).updateUserByUsernameWithRole(NEW_USER_EMAIL, "ROLE_SELF_REMOVING");
    }

    @Test
    void testLoginUserWithOAuth2_WhenDataCorrect_Successfully() {
        var oauth2User = createDefaultOidcUser();
        userService.loginUserWithOAuth2(oauth2User);

        verify(userRepository).findUserProjectionByEmail(oauth2UserArgumentCaptor.capture());
        assertEquals(NEW_USER_EMAIL, oauth2UserArgumentCaptor.getValue());
    }

    @Test
    void makeAccountActiveAgain_WhenDataCorrect_Successfully() {
        userWithOldPassword.setUsername("BoB");
        userService.makeAccountActiveAgain(userWithOldPassword.getUsername());

        verify(roleService).setUserRoleToUserByUsername(usernameArgumentCaptor.capture());
        assertEquals(userWithOldPassword.getUsername(), usernameArgumentCaptor.getValue());
    }

    @Test
    void testPermanentlyDeleteUsers_ShouldDeleteRequiredUsers() {
        List<User> users = createTestUsers();
        assertEquals(2, users.size());
        when(userRepository.findByRole_Name(anyString())).thenReturn(users);

        userService.permanentlyDeleteUsers();

        verify(userRepository).delete(users.get(0));
        verify(userRepository).delete(users.get(1));
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();
        userWithOldPassword.setUsername(NEW_USER_EMAIL);
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));
        userWithOldPassword.setUpdated(LocalDateTime.now());

        return userWithOldPassword;
    }

    private List<User> createTestUsers() {
        final var roleSelfRemoving = new Role(UUID.randomUUID(), "ROLE_SELF_REMOVING", List.of());

        User shouldBeDeletedHaveRoleSelfRemovingAndExpiredDate = createUserForDeleting(
                roleSelfRemoving, numberOfDaysToKeepDeletedUsers + 1);
        User shouldNotBeDeletedHaveRoleSelfRemovingAndUnexpiredDate = createUserForDeleting(
                roleSelfRemoving, numberOfDaysToKeepDeletedUsers - 1);

        return List.of(shouldBeDeletedHaveRoleSelfRemovingAndExpiredDate, shouldNotBeDeletedHaveRoleSelfRemovingAndUnexpiredDate);
    }

    private User createUserForDeleting(Role role, int delay) {
        User user = new User();
        user.setRole(role);
        user.setUpdated(LocalDateTime.now().minusDays(delay));

        return user;
    }

    @ParameterizedTest
    @MethodSource("getTestLocales")
    void updatePreferableLanguage_shouldSetLanguageAccordingContext(Locale expectedLocale) {
        LocaleContextHolder.setLocale(expectedLocale);
        final var user = new User();
        user.setLanguage(Locale.FRANCE);
        when(userRepository.findByRefreshToken_Token(anyString())).thenReturn(Optional.of(user));

        userService.updatePreferableLanguage("mocked token");
        verify(userRepository).saveAndFlush(userArgumentCaptor.capture());
        assertEquals(expectedLocale, userArgumentCaptor.getValue().getLanguage());
    }

    private static List<Locale> getTestLocales() {
        return List.of(Locale.ENGLISH, Locale.of("ua"), Locale.of("ru"));
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
                "family_name", USER_LAST_NAME));

        return new DefaultOidcUser(Collections.singletonList(new SimpleGrantedAuthority(roleUser)), idToken, userInfo);
    }
}
