package space.obminyashka.items_exchange.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.EmailSendingException;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthService authService;
    @Mock
    private MailService mailService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MessageSource messageSource;
    @Captor
    private ArgumentCaptor<String> usernameCaptor;
    @Captor
    private ArgumentCaptor<UserLoginResponseDto> authCaptor;
    @InjectMocks
    private AuthController authController;
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    private UserRegistrationDto dto;

    @BeforeEach
    void setUp() {
        dto = new UserRegistrationDto("user", "user@mail.ua", "pass", "pass");
        messageSourceUtil.setMSource(messageSource);
    }

    @Test
    void register_whenAllServicesPositiveFlow_shouldReturnCreated() throws Exception {
        when(userService.registerNewUser(any(), any())).thenReturn(true);

        final var responseEntity = authController.registerUser(dto, HttpHeaders.HOST);

        assertAll("Verify invoking services one by one and expected status",
                () -> verify(userService).existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()),
                () -> verify(mailService).sendEmailTemplateAndGenerateConfrimationCode(eq(dto.getEmail()), eq(EmailType.REGISTRATION), any()),
                () -> verify(userService).registerNewUser(eq(dto), any()),
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()));
    }

    @Test
    void register_whenMailServiceFailed_shouldThrowEmailSendingException() {
        doThrow(new EmailSendingException("Expected exception!"))
                .when(mailService)
                .sendEmailTemplateAndGenerateConfrimationCode(anyString(), any(), any());


        assertThrows(EmailSendingException.class, () -> authController.registerUser(dto, HttpHeaders.HOST));

        assertAll("Verify invoking services one by one and expected status",
                () -> verify(userService).existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()),
                () -> verify(mailService).sendEmailTemplateAndGenerateConfrimationCode(eq(dto.getEmail()), eq(EmailType.REGISTRATION), any()),
                () -> verifyNoMoreInteractions(userService));
    }

    @Test
    void login_shouldLoginUser() {
        var expectedUserDto = new UserLoginDto();
        expectedUserDto.setUsernameOrEmail("user");
        var prefinalizeUserLoginDto = createUserLoginDto();
        var expectedUserLoginDto = finalizeUserLoginDto(prefinalizeUserLoginDto);
        when(userService.findAuthDataByUsernameOrEmail(any())).thenReturn(prefinalizeUserLoginDto);
        when(authService.finalizeAuthData(any())).thenReturn(expectedUserLoginDto);

        var actualUserLoginDto = authController.login(expectedUserDto);

        assertAll(
                () -> verify(userService).findAuthDataByUsernameOrEmail(usernameCaptor.capture()),
                () -> assertEquals(expectedUserLoginDto.getUsername(), usernameCaptor.getValue()),
                () -> verify(authService).finalizeAuthData(authCaptor.capture()),
                () -> assertEquals(prefinalizeUserLoginDto, authCaptor.getValue()),
                () -> verify(authenticationManager).authenticate(any()),
                () -> assertThat(actualUserLoginDto)
                        .hasFieldOrPropertyWithValue("username", expectedUserLoginDto.getUsername())
                        .hasFieldOrPropertyWithValue("email", expectedUserLoginDto.getEmail())
                        .hasFieldOrPropertyWithValue("firstName", expectedUserLoginDto.getFirstName())
                        .hasFieldOrPropertyWithValue("lastName", expectedUserLoginDto.getLastName())
                        .hasFieldOrPropertyWithValue("language", expectedUserLoginDto.getLanguage())
                        .hasFieldOrPropertyWithValue("role", expectedUserLoginDto.getRole())
                        .hasFieldOrPropertyWithValue("accessToken", expectedUserLoginDto.getAccessToken())
                        .hasFieldOrPropertyWithValue("accessTokenExpirationDate", expectedUserLoginDto.getAccessTokenExpirationDate())
                        .hasFieldOrPropertyWithValue("refreshToken", expectedUserLoginDto.getRefreshToken())
                        .hasFieldOrPropertyWithValue("refreshTokenExpirationDate", expectedUserLoginDto.getRefreshTokenExpirationDate())
                        .hasFieldOrPropertyWithValue("avatarImage", expectedUserLoginDto.getAvatarImage())
        );
    }

    @Test
    void login_shouldBadCredentialsException() {
        var expectedUserDto = new UserLoginDto();
        expectedUserDto.setUsernameOrEmail("user");
        var prefinalizelUserLoginDto = createUserLoginDto();
        var expectedUserLoginDto = finalizeUserLoginDto(prefinalizelUserLoginDto);
        when(userService.findAuthDataByUsernameOrEmail(any())).thenReturn(prefinalizelUserLoginDto);
        when(authService.finalizeAuthData(any())).thenReturn(expectedUserLoginDto);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        assertAll(
                () -> assertThrows(
                        BadCredentialsException.class,
                        () -> authController.login(expectedUserDto)),
                () -> verify(userService).findAuthDataByUsernameOrEmail(usernameCaptor.capture()),
                () -> assertEquals(expectedUserLoginDto.getUsername(), usernameCaptor.getValue()),
                () -> verify(authService).finalizeAuthData(authCaptor.capture()),
                () -> assertEquals(expectedUserLoginDto, authCaptor.getValue()),
                () -> verify(authenticationManager).authenticate(any())
        );
    }

    @Test
    void loginWithOAuth2_shouldLoginUser() {
        var mockAuth = new UsernamePasswordAuthenticationToken("user", "1234");
        var prefinalizeUserLoginDto = createUserLoginDto();
        var expectedUserLoginDto = finalizeUserLoginDto(prefinalizeUserLoginDto);
        when(userService.findAuthDataByUsernameOrEmail(any())).thenReturn(prefinalizeUserLoginDto);
        when(authService.finalizeAuthData(any())).thenReturn(expectedUserLoginDto);

        var actualUserLoginDto = authController.loginWithOAuth2(mockAuth);

        assertAll(
                () -> verify(userService).findAuthDataByUsernameOrEmail(usernameCaptor.capture()),
                () -> assertEquals(mockAuth.getName(), usernameCaptor.getValue()),
                () -> verify(authService).finalizeAuthData(authCaptor.capture()),
                () -> assertEquals(prefinalizeUserLoginDto, authCaptor.getValue()),
                () -> assertThat(actualUserLoginDto)
                        .hasFieldOrPropertyWithValue("username", expectedUserLoginDto.getUsername())
                        .hasFieldOrPropertyWithValue("email", expectedUserLoginDto.getEmail())
                        .hasFieldOrPropertyWithValue("firstName", expectedUserLoginDto.getFirstName())
                        .hasFieldOrPropertyWithValue("lastName", expectedUserLoginDto.getLastName())
                        .hasFieldOrPropertyWithValue("language", expectedUserLoginDto.getLanguage())
                        .hasFieldOrPropertyWithValue("role", expectedUserLoginDto.getRole())
                        .hasFieldOrPropertyWithValue("accessToken", expectedUserLoginDto.getAccessToken())
                        .hasFieldOrPropertyWithValue("accessTokenExpirationDate", expectedUserLoginDto.getAccessTokenExpirationDate())
                        .hasFieldOrPropertyWithValue("refreshToken", expectedUserLoginDto.getRefreshToken())
                        .hasFieldOrPropertyWithValue("refreshTokenExpirationDate", expectedUserLoginDto.getRefreshTokenExpirationDate())
                        .hasFieldOrPropertyWithValue("avatarImage", expectedUserLoginDto.getAvatarImage())
        );
    }

    @Test
    void loginWithOAuth2_shouldBadCredentialsException() {
        var mockAuth = new UsernamePasswordAuthenticationToken("user", "1234");
        when(userService.findAuthDataByUsernameOrEmail(any())).thenThrow(new UsernameNotFoundException(""));

        assertAll(
                () -> assertThrows(
                        BadCredentialsException.class,
                        () -> authController.loginWithOAuth2(mockAuth)),
                () -> verify(userService).findAuthDataByUsernameOrEmail(usernameCaptor.capture()),
                () -> assertEquals(mockAuth.getName(), usernameCaptor.getValue()),
                () -> verify(authService, times(0)).finalizeAuthData(any())
        );
    }

    private UserLoginResponseDto createUserLoginDto() {
        var role = new Role();
        role.setName("ROLE_USER");
        var userLoginDto = new UserLoginResponseDto();
        userLoginDto.setUsername("user");
        userLoginDto.setEmail("email");
        userLoginDto.setFirstName("name");
        userLoginDto.setLastName("name");
        userLoginDto.setRole(role);
        userLoginDto.setLanguage("ua");
        userLoginDto.setRefreshToken("refresh_token");
        userLoginDto.setRefreshTokenExpirationDate(LocalDateTime.now().toString());
        userLoginDto.setAvatarImage(new byte[0]);
        return userLoginDto;
    }

    private UserLoginResponseDto finalizeUserLoginDto(UserLoginResponseDto userLoginDto) {
        userLoginDto.setAccessToken("access_token");
        userLoginDto.setAccessTokenExpirationDate(LocalDateTime.now());
        userLoginDto.setRefreshToken("refresh_token");
        userLoginDto.setRefreshTokenExpirationDate(LocalDateTime.now().toString());
        return userLoginDto;
    }
}