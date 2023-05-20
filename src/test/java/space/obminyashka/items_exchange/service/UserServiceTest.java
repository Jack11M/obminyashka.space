package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.mapper.PhoneMapper;
import space.obminyashka.items_exchange.mapper.UserMapper;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final String NEW_USER_EMAIL = "user@mail.ua";

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailConfirmationCodeRepository emailConfirmationCodeRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private PhoneMapper phoneMapper;
    @Mock
    private RoleService roleService;
    @Mock
    private UserMapper userMapper;
    @Captor
    private ArgumentCaptor<String> oauth2UserArgumentCaptor;
    private UserServiceImpl userService;
    @Value("${number.of.hours.to.keep.email.confirmation.code}")
    private int numberOfHoursToKeepEmailConformationToken;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(bCryptPasswordEncoder,userRepository, emailConfirmationCodeRepository, phoneMapper, roleService, userMapper, numberOfHoursToKeepEmailConformationToken);
    }

    @Test
    void testLoginUserWithOAuth2_WhenUserBeenCreated() {
        var oauth2User = createDefaultOidcUser();
        when(userRepository.findByEmailOrUsername(NEW_USER_EMAIL, NEW_USER_EMAIL)).thenReturn(creatOptionalUser());
        userService.loginUserWithOAuth2(oauth2User);

        verify(userRepository).setOAuth2LoginToUserByEmail(oauth2UserArgumentCaptor.capture());

        assertEquals(NEW_USER_EMAIL, oauth2UserArgumentCaptor.getValue());
    }

    @Test
    void testLoginUserWithOAuth2_WhenUserBeenCreatedAndHasValidatedEmailWithOauth2Login() {
        var oauth2User = createDefaultOidcUser();
        var optionalUser = creatOptionalUser();
        optionalUser.get().isValidatedEmail(true);
        optionalUser.get().setOauth2Login(true);

        when(userRepository.findByEmailOrUsername(NEW_USER_EMAIL, NEW_USER_EMAIL)).thenReturn(optionalUser);
        userService.loginUserWithOAuth2(oauth2User);

        verify(userRepository, never()).setOAuth2LoginToUserByEmail(oauth2UserArgumentCaptor.capture());
    }



    private Optional<User> creatOptionalUser() {
        User user = new User();
        user.setEmail(NEW_USER_EMAIL);
        user.setUsername("First");
        user.setOauth2Login(null);
        return Optional.of(user);
    }

    private DefaultOidcUser createDefaultOidcUser() {
        var roleUser = "ROLE_USER";
        String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I";

        final var now = Instant.now();
        var idToken = new OidcIdToken(
                ID_TOKEN,
                now,
                now.plusSeconds(60),
                Map.of("groups", roleUser, "sub", 123));

        final var userInfo = new OidcUserInfo(Map.of(
                "email", NEW_USER_EMAIL,
                "given_name", "First",
                "family_name", "Last"));

        return new DefaultOidcUser(Collections.singletonList(new SimpleGrantedAuthority(roleUser)), idToken, userInfo);
    }
}
