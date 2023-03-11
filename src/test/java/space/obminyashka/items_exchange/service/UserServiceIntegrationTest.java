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
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@SpringBootTest
class UserServiceIntegrationTest {

    public static final String CORRECT_OLD_PASSWORD = "123456xX";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String NEW_USER_EMAIL = "user@mail.ru";
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
    void testUpdateUserPassword_WhenDataCorrect_Successfully() {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        String message = userService.updateUserPassword(userChangePasswordDto, userWithOldPassword);

        assertEquals(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_PASSWORD), message);
        assertTrue(bCryptPasswordEncoder.matches(NEW_PASSWORD, userWithOldPassword.getPassword()));
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testUpdateUserEmail_WhenDataCorrect_Successfully() {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_USER_EMAIL, NEW_USER_EMAIL);
        String message = userService.updateUserEmail(userChangeEmailDto, userWithOldPassword);

        assertEquals(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_EMAIL), message);
        assertEquals(NEW_USER_EMAIL, userWithOldPassword.getEmail());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testSelfDeleteRequest_WhenDataCorrect_Successfully() {
        when(roleService.getRole(anyString())).thenReturn(Optional.of(new Role(UUID.randomUUID(), "ROLE_SELF_REMOVING", List.of())));

        userService.selfDeleteRequest(userWithOldPassword);

        assertEquals("ROLE_SELF_REMOVING", userWithOldPassword.getRole().getName());
        verify(userRepository).saveAndFlush(userWithOldPassword);

    }

    @Test
    void testLoginUserWithOAuth2_WhenDataCorrect_Successfully() {
        var oauth2User = createDefaultOidcUser();
        userService.loginUserWithOAuth2(oauth2User);

        verify(userRepository).findByEmailOrUsername(oauth2UserArgumentCaptor.capture(), oauth2UserArgumentCaptor.capture());
        assertEquals(NEW_USER_EMAIL,oauth2UserArgumentCaptor.getValue());
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
        assertEquals(4, users.size());

        when(userRepository.findAll()).thenReturn(users);
        userService.permanentlyDeleteUsers();

        verify(userRepository).delete(users.get(0));

        for (int i = 1; i < users.size(); i++) {
            verify(userRepository, never()).delete(users.get(i));
        }
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));
        userWithOldPassword.setUpdated(LocalDateTime.now());

        return userWithOldPassword;
    }

    private List<User> createTestUsers() {
        when(roleService.getRole("ROLE_SELF_REMOVING")).thenReturn(Optional.of(new Role(UUID.randomUUID(), "ROLE_SELF_REMOVING", List.of())));
        when(roleService.getRole("ROLE_USER")).thenReturn(Optional.of(new Role(UUID.randomUUID(), "ROLE_USER", List.of())));

        User shouldBeDeletedHaveRoleSelfRemovingAndExpiredDate = createUserForDeleting(roleService.getRole("ROLE_SELF_REMOVING").get(), numberOfDaysToKeepDeletedUsers + 1);
        User shouldNotBeDeletedHaveRoleUserAndExpiredDate = createUserForDeleting(roleService.getRole("ROLE_USER").get(), 0);
        User shouldNotBeDeletedHaveRoleSelfRemovingAndUnexpiredDate = createUserForDeleting(roleService.getRole("ROLE_SELF_REMOVING").get(), numberOfDaysToKeepDeletedUsers - 1);
        User shouldNotBeDeletedHaveRoleUserAndUnexpiredDate = createUserForDeleting(roleService.getRole("ROLE_USER").get(), numberOfDaysToKeepDeletedUsers + 1);

        return List.of(shouldBeDeletedHaveRoleSelfRemovingAndExpiredDate, shouldNotBeDeletedHaveRoleUserAndExpiredDate, shouldNotBeDeletedHaveRoleSelfRemovingAndUnexpiredDate, shouldNotBeDeletedHaveRoleUserAndUnexpiredDate);
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
        return List.of(Locale.ENGLISH, new Locale("ua"), new Locale("ru"));
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
