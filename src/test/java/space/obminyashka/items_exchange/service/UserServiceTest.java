package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.mapper.PhoneMapper;
import space.obminyashka.items_exchange.mapper.UserMapper;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.projection.UserProjection;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
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
    private UserServiceImpl userService;
    @Value("${number.of.hours.to.keep.email.confirmation.code}")
    private int numberOfHoursToKeepEmailConformationToken;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(bCryptPasswordEncoder, userRepository, emailConfirmationCodeRepository, phoneMapper, roleService, userMapper, numberOfHoursToKeepEmailConformationToken);
    }

    @Test
    void testLoginUserWithOAuth2_WhenUserBeenCreatedAndHasValidatedEmailWithOauth2Login() {
        //Arrange

        var oauth2User = createDefaultOidcUser();
        var userProjection = creatUserProjection();
        when(userRepository.findUserProjectionByEmail(NEW_USER_EMAIL)).thenReturn(Optional.of(userProjection));
        when(userMapper.toUserFromProjection(userProjection)).thenReturn(creatUser());

        //Act
        User user = userService.loginUserWithOAuth2(oauth2User);

        //Assert
        assertAll(
                () -> assertEquals(user.getUsername(), userProjection.getUsername()),
                () -> assertEquals(user.getRole().getName(), userProjection.getRole().getName()));
    }

    private UserProjection creatUserProjection() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Role role = new Role();
        role.setName("ROLE_USER");
        Map<String, Object> map = Map.of(
                "username", "First",
                "role", role
        );
        return factory.createProjection(UserProjection.class, map);
    }

    private User creatUser() {
        Role role = new Role();
        role.setName("ROLE_USER");
        User user = new User();
        user.setRole(role);
        user.setEmail(NEW_USER_EMAIL);
        user.setUsername("First");
        user.setOauth2Login(null);
        return user;
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
