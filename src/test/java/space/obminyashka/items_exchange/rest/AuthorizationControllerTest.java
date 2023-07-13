package space.obminyashka.items_exchange.rest;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import space.obminyashka.items_exchange.repository.model.Role;
import space.obminyashka.items_exchange.rest.exception.EmailSendingException;
import space.obminyashka.items_exchange.rest.request.UserLoginRequest;
import space.obminyashka.items_exchange.rest.request.UserRegistrationRequest;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;
import space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.service.util.EmailType;

import java.time.LocalDateTime;

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
    @InjectMocks
    private AuthController authController;
    @InjectMocks
    private MessageSourceProxy messageSourceProxy;
    private UserRegistrationRequest dto;

    @BeforeEach
    void setUp() {
        dto = new UserRegistrationRequest("user", "user@mail.ua", "pass", "pass");
        messageSourceProxy.setMSource(messageSource);
    }

    @Test
    void register_whenAllServicesPositiveFlow_shouldReturnCreated() {
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
    void login_whenUserWihUserNameExistsInDB_shouldLoginUser() {
        var expectedUserDto = createUserLoginDto();
        var prefinalizeUserLoginDto = createUserLoginResponseDto();
        var expectedUserLoginDto = finalizeUserLoginDto(prefinalizeUserLoginDto);
        when(userService.findAuthDataByUsernameOrEmail(anyString())).thenReturn(prefinalizeUserLoginDto);
        when(authService.finalizeAuthData(any(UserLoginResponse.class))).thenReturn(expectedUserLoginDto);

        var actualUserLoginDto = authController.login(expectedUserDto);

        assertAll(
                () -> assertEquals(expectedUserLoginDto, actualUserLoginDto),
                () -> verify(userService).findAuthDataByUsernameOrEmail(expectedUserDto.getUsernameOrEmail()),
                () -> verify(authService).finalizeAuthData(prefinalizeUserLoginDto),
                () -> verify(authenticationManager).authenticate(any())
        );
    }

    @Test
    void login_whenUserWihUserNameDoesNotExistInDB_shouldBadCredentialsException() {
        var expectedUserDto = createUserLoginDto();
        var prefinalizelUserLoginDto = createUserLoginResponseDto();
        when(userService.findAuthDataByUsernameOrEmail(anyString())).thenReturn(prefinalizelUserLoginDto);
        when(authService.finalizeAuthData(any(UserLoginResponse.class)))
                .thenReturn(finalizeUserLoginDto(prefinalizelUserLoginDto));
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        assertAll(
                () -> assertThrows(
                        BadCredentialsException.class,
                        () -> authController.login(expectedUserDto)),
                () -> verify(userService).findAuthDataByUsernameOrEmail(expectedUserDto.getUsernameOrEmail()),
                () -> verify(authService).finalizeAuthData(prefinalizelUserLoginDto),
                () -> verify(authenticationManager).authenticate(any())
        );
    }

    private UserLoginRequest createUserLoginDto() {
        var dto = new UserLoginRequest();
        dto.setUsernameOrEmail("user");
        return dto;
    }

    @Test
    void loginWithOAuth2_whenUserWihUserNameExistsInDB_shouldLoginUser() {
        var mockAuth = new UsernamePasswordAuthenticationToken("user", "1234");
        var prefinalizeUserLoginDto = createUserLoginResponseDto();
        var expectedUserLoginDto = finalizeUserLoginDto(prefinalizeUserLoginDto);
        when(userService.findAuthDataByUsernameOrEmail(anyString())).thenReturn(prefinalizeUserLoginDto);
        when(authService.finalizeAuthData(any(UserLoginResponse.class))).thenReturn(expectedUserLoginDto);

        var actualUserLoginDto = authController.loginWithOAuth2(mockAuth);

        assertAll(
                () -> assertEquals(expectedUserLoginDto, actualUserLoginDto),
                () -> verify(userService).findAuthDataByUsernameOrEmail(mockAuth.getName()),
                () -> verify(authService).finalizeAuthData(prefinalizeUserLoginDto)
        );
    }

    @Test
    void loginWithOAuth2_whenUserWihUserNameDoesNotExistInDB_shouldBadCredentialsException() {
        var mockAuth = new UsernamePasswordAuthenticationToken("user", "1234");
        when(userService.findAuthDataByUsernameOrEmail(anyString())).thenThrow(new UsernameNotFoundException(""));

        assertAll(
                () -> assertThrows(
                        BadCredentialsException.class,
                        () -> authController.loginWithOAuth2(mockAuth)),
                () -> verify(userService).findAuthDataByUsernameOrEmail(mockAuth.getName()),
                () -> verifyNoInteractions(authService)
        );
    }

    private UserLoginResponse createUserLoginResponseDto() {
        var role = new Role();
        role.setName("ROLE_USER");
        var userLoginDto = new UserLoginResponse();
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

    private UserLoginResponse finalizeUserLoginDto(UserLoginResponse userLoginDto) {
        userLoginDto.setAccessToken("access_token");
        userLoginDto.setAccessTokenExpirationDate(LocalDateTime.now());
        userLoginDto.setRefreshToken("refresh_token");
        userLoginDto.setRefreshTokenExpirationDate(LocalDateTime.now().toString());
        return userLoginDto;
    }
}