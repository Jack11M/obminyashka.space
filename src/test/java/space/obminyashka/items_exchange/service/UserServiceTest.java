package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.repository.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.repository.UserRepository;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;
import space.obminyashka.items_exchange.rest.mapper.PhoneMapper;
import space.obminyashka.items_exchange.rest.mapper.UserMapper;
import space.obminyashka.items_exchange.repository.model.RefreshToken;
import space.obminyashka.items_exchange.repository.model.Role;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.repository.projection.UserAuthProjection;
import space.obminyashka.items_exchange.repository.projection.UserProjection;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String NEW_USER_EMAIL = "user@mail.ua";
    private static final String EXPECTED_USERNAME = "user";
    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private final Role role = new Role(null, "ROLE_USER", Collections.emptyList());
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
    @Value("${number.of.hours.to.keep.email.confirmation.code}")
    private int numberOfHoursToKeepEmailConformationToken;
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(bCryptPasswordEncoder, userRepository, emailConfirmationCodeRepository,
                phoneMapper, roleService, userMapper, numberOfHoursToKeepEmailConformationToken);
    }

    @Test
    void testLoginUserWithOAuth2_WhenUserBeenCreated() {
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
        Map<String, Object> map = Map.of(
                "username", "First",
                "role", role
        );
        return factory.createProjection(UserProjection.class, map);
    }

    private User creatUser() {
        User user = new User();
        user.setRole(role);
        user.setUsername("First");
        return user;
    }

    private DefaultOidcUser createDefaultOidcUser() {
        String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I";

        final var now = Instant.now();
        var idToken = new OidcIdToken(
                ID_TOKEN,
                now,
                now.plusSeconds(60),
                Map.of("groups", role.getName(), "sub", 123));

        final var userInfo = new OidcUserInfo(Map.of(
                "email", NEW_USER_EMAIL,
                "given_name", "First",
                "family_name", "Last"));

        return new DefaultOidcUser(Collections.singletonList(new SimpleGrantedAuthority(role.getName())), idToken, userInfo);
    }

    @Test
    void findAuthDataByUsernameOrEmail_whenUserDoesNotExistInDB_shouldThrowException() {
        when(userRepository.findAuthDataByEmailOrUsername(anyString(), anyString())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> userService.findAuthDataByUsernameOrEmail(EXPECTED_USERNAME))
                        .isInstanceOf(UsernameNotFoundException.class)
                        .hasMessage("User " + EXPECTED_USERNAME + " is not logged in"),
                () -> verify(userRepository).findAuthDataByEmailOrUsername(EXPECTED_USERNAME, EXPECTED_USERNAME),
                () -> verifyNoInteractions(userMapper)
        );
    }

    @Test
    void findAuthDataByUsernameOrEmail_whenUserExistsInDB_shouldReturnUserLoginResponseDto() {
        var expectedProjection = creatUserAuthProjection();
        var expectedUserLoginDto = createUserLoginDto(expectedProjection);
        when(userRepository.findAuthDataByEmailOrUsername(anyString(), anyString()))
                .thenReturn(Optional.of(expectedProjection));
        when(userMapper.toLoginResponseDto(expectedProjection)).thenReturn(expectedUserLoginDto);

        var actualUserLoginDto = userService.findAuthDataByUsernameOrEmail(EXPECTED_USERNAME);

        assertAll(
                () -> verify(userRepository).findAuthDataByEmailOrUsername(EXPECTED_USERNAME, EXPECTED_USERNAME),
                () -> verify(userMapper).toLoginResponseDto(expectedProjection),
                () -> assertEquals(expectedUserLoginDto, actualUserLoginDto)
        );
    }

    private UserAuthProjection creatUserAuthProjection() {
        RefreshToken refreshToken = new RefreshToken("token", LocalDateTime.now());
        Map<String, Object> map = Map.of(
                "id", UUID.randomUUID(),
                "username", EXPECTED_USERNAME,
                "email", "email",
                "firstName", "name",
                "lastName", "name",
                "language", "ua",
                "role", role,
                "refreshToken", refreshToken,
                "avatarImage", new byte[0]
        );
        return factory.createProjection(UserAuthProjection.class, map);
    }

    private UserLoginResponse createUserLoginDto(UserAuthProjection projection) {
        var dto = new UserLoginResponse();
        BeanUtils.copyProperties(projection, dto);
        dto.setLanguage(projection.getLanguage().toString());
        dto.setRefreshToken(projection.getRefreshToken().getToken());
        dto.setRefreshTokenExpirationDate(projection.getRefreshToken().getExpiryDate().toString());
        return dto;
    }
}
