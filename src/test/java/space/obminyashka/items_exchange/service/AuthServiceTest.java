package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.service.impl.AuthServiceImpl;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private MessageSource messageSource;
    @Captor
    private ArgumentCaptor<String> usernameCaptor;
    @Captor
    private ArgumentCaptor<String> tokenCaptor;
    @Captor
    private ArgumentCaptor<Role> roleCaptor;
    @InjectMocks
    private AuthServiceImpl authService;
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    private final String JWT_TOKEN = "super_secret_token";
    private final String REFRESH_TOKEN = "refresh_token";

    @BeforeEach
    void init() {
        messageSourceUtil.setMSource(messageSource);
    }

    @Test
    void finalizeAuthData_shouldPopulateDtoAndCreateTokenWithUsername() {
        var userLoginResponseDto = new UserLoginResponseDto();
        userLoginResponseDto.setEmail("user@mail.ua");
        userLoginResponseDto.setUsername("user");
        userLoginResponseDto.setRole(new Role(UUID.randomUUID(), "ROLE_USER", List.of()));
        when(jwtTokenService.createAccessToken(anyString(), any())).thenReturn(JWT_TOKEN);
        when(jwtTokenService.getAccessTokenExpiration(JWT_TOKEN)).thenReturn(LocalDateTime.MAX);
        when(refreshTokenService.createRefreshToken(null, userLoginResponseDto.getUsername()))
                .thenReturn(new RefreshToken(REFRESH_TOKEN, LocalDateTime.MAX));
        when(jwtTokenService.getRefreshTokenExpiration(any())).thenReturn(REFRESH_TOKEN);

        final var actualUserLoginResponseDto = authService.finalizeAuthData(userLoginResponseDto);

        assertAll(
                () -> assertThat(actualUserLoginResponseDto)
                        .hasFieldOrPropertyWithValue("accessToken", JWT_TOKEN)
                        .hasFieldOrPropertyWithValue("refreshToken", REFRESH_TOKEN)
                        .hasFieldOrProperty("accessTokenExpirationDate")
                        .hasFieldOrProperty("refreshTokenExpirationDate"),
                () -> verify(jwtTokenService).createAccessToken(usernameCaptor.capture(), roleCaptor.capture()),
                () -> assertEquals(userLoginResponseDto.getUsername(), usernameCaptor.getValue()),
                () -> assertEquals(userLoginResponseDto.getRole(), roleCaptor.getValue()),
                () -> verify(jwtTokenService).getAccessTokenExpiration(any()),
                () -> verify(refreshTokenService).createRefreshToken(any(), usernameCaptor.capture()),
                () -> assertEquals(userLoginResponseDto.getUsername(), usernameCaptor.getValue()),
                () -> verify(jwtTokenService).getRefreshTokenExpiration(any())
        );
    }

    @Test
    void logout_shouldSuccessLogout() {
        var expectedAccessToken = "token";
        var expectedUsername = "user";

        final var isLogout = authService.logout(expectedAccessToken, expectedUsername);

        assertAll(
                () -> verify(jwtTokenService).invalidateAccessToken(tokenCaptor.capture()),
                () -> assertEquals(expectedAccessToken, tokenCaptor.getValue()),
                () -> verify(refreshTokenService).deleteByUsername(usernameCaptor.capture()),
                () -> assertEquals(expectedUsername, usernameCaptor.getValue()),
                () -> assertTrue(isLogout)
        );
    }

    @Test
    void logout_shouldWrongLogout() {
        var expectedAccessToken = "";
        var expectedUsername = "user";

        final var isLogout = authService.logout(expectedAccessToken, expectedUsername);

        assertAll(
                () -> verify(jwtTokenService, times(0)).invalidateAccessToken(any()),
                () -> verify(refreshTokenService, times(0)).deleteByUsername(any()),
                () -> assertFalse(isLogout)
        );
    }

    @Test
    void renewAccessTokenByRefresh_shouldCreateNewRefreshToken() throws RefreshTokenException {
        var expectedAccessToken = "access_token";
        var actualRefreshToken = "refresh_token";
        when(refreshTokenService.renewAccessTokenByRefresh(any())).thenReturn(Optional.ofNullable(expectedAccessToken));

        var refreshTokenDto = authService.renewAccessTokenByRefresh(actualRefreshToken);

        assertAll(
                () -> assertThat(refreshTokenDto)
                        .hasFieldOrPropertyWithValue("accessToken", expectedAccessToken)
                        .hasFieldOrPropertyWithValue("refreshToken", actualRefreshToken)
                        .hasFieldOrProperty("accessTokenExpiration")
                        .hasFieldOrProperty("refreshTokenExpiration"),
                () -> verify(refreshTokenService).renewAccessTokenByRefresh(tokenCaptor.capture()),
                () -> assertEquals(actualRefreshToken, tokenCaptor.getValue()),
                () -> verify(jwtTokenService).getAccessTokenExpiration(tokenCaptor.capture()),
                () -> assertEquals(expectedAccessToken, tokenCaptor.getValue()),
                () -> verify(jwtTokenService).getRefreshTokenExpiration(any())
        );
    }

    @Test
    void renewAccessTokenByRefresh_shouldThrowRefreshTokenException() {
        var expectedAccessToken = "";
        var actualRefreshToken = "";
        when(refreshTokenService.renewAccessTokenByRefresh(any())).thenReturn(Optional.ofNullable(expectedAccessToken));

        assertAll(
                () -> assertThrows(
                        RefreshTokenException.class,
                        () -> authService.renewAccessTokenByRefresh(actualRefreshToken)),
                () -> verify(refreshTokenService).renewAccessTokenByRefresh(tokenCaptor.capture()),
                () -> assertEquals(expectedAccessToken, tokenCaptor.getValue()),
                () -> verify(jwtTokenService, times(0)).getAccessTokenExpiration(any()),
                () -> verify(jwtTokenService, times(0)).getRefreshTokenExpiration(any())
        );
    }
}