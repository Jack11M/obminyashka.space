package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.mapper.PhoneMapper;
import space.obminyashka.items_exchange.mapper.UserMapper;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.projection.UserAuthProjection;
import space.obminyashka.items_exchange.model.projection.UserProjection;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    private ArgumentCaptor<String> usernameCaptor;
    @Captor
    private ArgumentCaptor<UserAuthProjection> projectionCaptor;
    private UserServiceImpl userService;
    @Value("${number.of.hours.to.keep.email.confirmation.code}")
    private int numberOfHoursToKeepEmailConformationToken;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(bCryptPasswordEncoder, userRepository, emailConfirmationCodeRepository, phoneMapper, roleService, userMapper, numberOfHoursToKeepEmailConformationToken);
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
        user.setUsername("First");
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

    @Test
    void findAuthDataByUsernameOrEmail_shouldThrowUsernameNotFoundException() {
        var expectedUsername = "user";
        when(userRepository.findAuthDataByEmailOrUsername(any(), any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(
                        UsernameNotFoundException.class,
                        () -> userService.findAuthDataByUsernameOrEmail(expectedUsername)),
                () -> verify(userRepository).findAuthDataByEmailOrUsername(usernameCaptor.capture(), any()),
                () -> assertEquals(expectedUsername, usernameCaptor.getValue()),
                () -> verify(userMapper, times(0)).toLoginResponseDto(any())
        );
    }

    @Test
    void findAuthDataByUsernameOrEmail_shouldReturnUserLoginResponseDto() {
        var expectedUsername = "user";
        var expectedProjection = creatUserAuthProjection();
        var expectedUserLoginDto = createUserLoginDto(expectedProjection.get());
        when(userRepository.findAuthDataByEmailOrUsername(any(), any())).thenReturn(expectedProjection);
        when(userMapper.toLoginResponseDto(any())).thenReturn(expectedUserLoginDto);

        var actualUserLoginDto = userService.findAuthDataByUsernameOrEmail(expectedUsername);

        assertAll(
                () -> verify(userRepository).findAuthDataByEmailOrUsername(usernameCaptor.capture(), any()),
                () -> assertEquals(expectedUsername, usernameCaptor.getValue()),
                () -> verify(userMapper).toLoginResponseDto(projectionCaptor.capture()),
                () -> assertThat(projectionCaptor.getValue())
                        .hasFieldOrPropertyWithValue("id", expectedProjection.get().getId())
                        .hasFieldOrPropertyWithValue("username", expectedProjection.get().getUsername())
                        .hasFieldOrPropertyWithValue("email", expectedProjection.get().getEmail())
                        .hasFieldOrPropertyWithValue("firstName", expectedProjection.get().getFirstName())
                        .hasFieldOrPropertyWithValue("lastName", expectedProjection.get().getLastName())
                        .hasFieldOrPropertyWithValue("language", expectedProjection.get().getLanguage())
                        .hasFieldOrPropertyWithValue("role", expectedProjection.get().getRole())
                        .hasFieldOrPropertyWithValue("refreshToken", expectedProjection.get().getRefreshToken())
                        .hasFieldOrPropertyWithValue("avatarImage", expectedProjection.get().getAvatarImage()),
                () -> assertThat(actualUserLoginDto)
                        .hasFieldOrPropertyWithValue("username", expectedUserLoginDto.getUsername())
                        .hasFieldOrPropertyWithValue("email", expectedUserLoginDto.getEmail())
                        .hasFieldOrPropertyWithValue("firstName", expectedUserLoginDto.getFirstName())
                        .hasFieldOrPropertyWithValue("lastName", expectedUserLoginDto.getLastName())
                        .hasFieldOrPropertyWithValue("language", expectedUserLoginDto.getLanguage())
                        .hasFieldOrPropertyWithValue("role", expectedUserLoginDto.getRole())
                        .hasFieldOrPropertyWithValue("refreshToken", expectedUserLoginDto.getRefreshToken())
                        .hasFieldOrPropertyWithValue("refreshTokenExpirationDate", expectedUserLoginDto.getRefreshTokenExpirationDate())
                        .hasFieldOrPropertyWithValue("avatarImage", expectedUserLoginDto.getAvatarImage())
        );
    }

    private Optional<UserAuthProjection> creatUserAuthProjection() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Role role = new Role();
        role.setName("ROLE_USER");
        RefreshToken refreshToken = new RefreshToken("token", LocalDateTime.now());
        Map<String, Object> map = Map.of(
                "id", UUID.randomUUID(),
                "username", "user",
                "email", "email",
                "firstName", "name",
                "lastName", "name",
                "language", "ua",
                "role", role,
                "refreshToken", refreshToken,
                "avatarImage", new byte[0]
        );
        return Optional.ofNullable(factory.createProjection(UserAuthProjection.class, map));
    }

    private UserLoginResponseDto createUserLoginDto(UserAuthProjection projection) {
        var userLoginDto = new UserLoginResponseDto();
        userLoginDto.setUsername(projection.getUsername());
        userLoginDto.setEmail(projection.getEmail());
        userLoginDto.setFirstName(projection.getFirstName());
        userLoginDto.setLastName(projection.getLastName());
        userLoginDto.setRole(projection.getRole());
        userLoginDto.setLanguage(projection.getLanguage().toString());
        userLoginDto.setRefreshToken(projection.getRefreshToken().getToken());
        userLoginDto.setRefreshTokenExpirationDate(projection.getRefreshToken().getExpiryDate().toString());
        userLoginDto.setAvatarImage(projection.getAvatarImage());
        return userLoginDto;
    }
}
