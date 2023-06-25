package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.dao.RefreshTokenRepository;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.impl.RefreshTokenServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Captor
    private ArgumentCaptor<String> usernameCaptor;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;


    @Test
    void createRefreshToken_whenRefreshTokenIsNullOrDoesNotExistInDB_shouldCreateToken() {
        var username = "user_without_token";
        var mockRefreshToken = new RefreshToken().setToken(null);
        var expectedRefreshToken = "token_string";
        mockJwtRefreshTokenData(expectedRefreshToken, LocalDateTime.MAX);

        final var actualRefreshToken = refreshTokenService.createRefreshToken(mockRefreshToken.getToken(), username);

        assertAll(
                () -> assertNotNull(actualRefreshToken),
                () -> assertEquals(expectedRefreshToken, actualRefreshToken.getToken()),
                () -> assertEquals(LocalDateTime.MAX, actualRefreshToken.getExpiryDate()),
                () -> verify(refreshTokenRepository).createRefreshToken(any(), any(), any())
        );
    }

    @Test
    void createRefreshToken_whenRefreshTokenExistsInDB_shouldUpdateToken() {
        var username = "user_with_token";
        var mockRefreshToken = new RefreshToken().setToken("");
        var expectedRefreshToken = "token_string";
        mockJwtRefreshTokenData(expectedRefreshToken, LocalDateTime.MAX);

        final var actualRefreshToken = refreshTokenService.createRefreshToken(mockRefreshToken.getToken(), username);

        assertAll(
                () -> assertNotNull(actualRefreshToken),
                () -> assertEquals(expectedRefreshToken, actualRefreshToken.getToken()),
                () -> assertEquals(LocalDateTime.MAX, actualRefreshToken.getExpiryDate()),
                () -> verify(refreshTokenRepository).updateRefreshToken(any(), any(), any())
        );
    }

    private void mockJwtRefreshTokenData(String token, LocalDateTime expiryDate) {
        when(jwtTokenService.generateRefreshToken(anyString())).thenReturn(token);
        when(jwtTokenService.generateRefreshTokenExpirationTime()).thenReturn(expiryDate);
    }

    @Test
    void renewAccessTokenByRefreshToken_whenAccessTokenExpires_shouldRenewAccessToken() {
        var expectedAccessToken = "some token";
        var expectedRenewStatus = true;
        var existRefreshToken = new RefreshToken().setExpiryDate(LocalDateTime.now().plusHours(1));
        existRefreshToken.setUser(
                new User()
                        .setUsername("user")
                        .setRole(new Role(UUID.randomUUID(), "ROLE_USER", List.of()))
        );
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.ofNullable(existRefreshToken));
        when(jwtTokenService.createAccessToken(any(), any())).thenReturn(expectedAccessToken);

        var actualAccessToken = refreshTokenService.renewAccessTokenByRefresh(existRefreshToken.getToken());

        assertAll(
                () -> assertEquals(expectedRenewStatus, actualAccessToken.isPresent()),
                () -> assertEquals(expectedAccessToken, actualAccessToken.get()),
                () -> verify(refreshTokenRepository).findByToken(any()),
                () -> verify(jwtTokenService).createAccessToken(any(), any())
        );
    }

    @Test
    void renewAccessTokenByRefreshToken_whenAccessTokenDoesNotExpiry_shouldNotRenewAccessToken() {
        var expectedRenewStatus = false;
        var existRefreshToken = new RefreshToken().setExpiryDate(LocalDateTime.now().minusHours(1));
        existRefreshToken.setUser(
                new User()
                        .setUsername("user")
                        .setRole(new Role(UUID.randomUUID(), "ROLE_USER", List.of()))
        );
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.ofNullable(existRefreshToken));

        var actualAccessToken = refreshTokenService.renewAccessTokenByRefresh(existRefreshToken.getToken());

        assertAll(
                () -> assertEquals(expectedRenewStatus, actualAccessToken.isPresent()),
                () -> assertTrue(actualAccessToken.isEmpty()),
                () -> verify(refreshTokenRepository).findByToken(any()),
                () -> verify(jwtTokenService, times(0)).createAccessToken(any(), any())
        );
    }

    @Test
    void deleteByUsername_whenUserWithUsernameExistsInDB_shouldDeleteRefreshToken() {
        var expectedUsername = "user";

        refreshTokenService.deleteByUsername(expectedUsername);

        assertAll(
                () -> verify(refreshTokenRepository).deleteByUserUsername(usernameCaptor.capture()),
                () -> assertEquals(expectedUsername, usernameCaptor.getValue())
        );
    }
}