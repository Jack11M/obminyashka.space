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
import org.springframework.scheduling.support.CronExpression;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    @Value("${cron.expression.once_per_day_at_3am}")
    private String selfRemovedUsersCleanupTime;
    private User userWithOldPassword;

    @BeforeEach
    void setUp() {
        userWithOldPassword = createUserWithOldPassword();
    }


    private User createUserWithOldPassword() {
        var user = new User().setUsername(NEW_USER_EMAIL);
        user.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));
        user.setUpdated(LocalDateTime.now());
        return user;
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
    void permanentlyDeleteUsers_shouldDeleteUserAndValidateCronSchedule() {
        List<User> userToRemove = List.of(new User().setRole(new Role(UUID.randomUUID(), "ROLE_SELF_REMOVING", List.of())));
        when(userRepository.findAllByUpdatedLessThanEqualAndRoleName(any(), anyString())).thenReturn(userToRemove);

        userService.permanentlyDeleteUsers();

        verify(userRepository).deleteAll(userToRemove);
        assertThat(CronExpression.parse(selfRemovedUsersCleanupTime)
                .next(LocalDateTime.of(2023, Calendar.JULY, 1, 9, 53, 50)))
                .as("Checking permanently delete users nearest scheduled date time")
                .isEqualTo(LocalDateTime.of(2023, Calendar.JULY, 2, 3, 0));
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
