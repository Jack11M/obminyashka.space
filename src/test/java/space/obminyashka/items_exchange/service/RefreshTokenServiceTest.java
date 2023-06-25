package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private final static String EXPECTED_USERNAME = "user";
    private final static String EXPECTED_ACCESS_TOKEN = "access_token";
    private final static String EXPECTED_REFRESH_TOKEN = "refresh_token";
    private final static int ADD_HOUR_TO_CURRENT_TIME = 1;
    private final static int MINUS_HOUR_FROM_CURRENT_TIME= -1;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;


    @Test
    void createRefreshToken_whenRefreshTokenIsNullOrDoesNotExistInDB_shouldCreateToken(RefreshToken refreshToken) {
        var mockRefreshToken = new RefreshToken().setToken(null);
        mockJwtRefreshTokenData(EXPECTED_REFRESH_TOKEN);

        final var actualRefreshToken = refreshTokenService.createRefreshToken(mockRefreshToken.getToken(), EXPECTED_USERNAME);

        assertAll(
                () -> assertEquals(EXPECTED_REFRESH_TOKEN, actualRefreshToken.getToken()),
                () -> verify(jwtTokenService).generateRefreshToken(EXPECTED_USERNAME),
                () -> verify(jwtTokenService).generateRefreshTokenExpirationTime(),
                () -> verify(refreshTokenRepository)
                        .createRefreshToken(eq(EXPECTED_USERNAME), eq(EXPECTED_REFRESH_TOKEN), any())
        );
    }

    @Test
    void createRefreshToken_whenRefreshTokenExistsInDB_shouldUpdateToken() {
        var expectedRefreshToken = new RefreshToken().setToken("");
        mockJwtRefreshTokenData(expectedRefreshToken.getToken());

        final var actualRefreshToken = refreshTokenService
                .createRefreshToken(expectedRefreshToken.getToken(), EXPECTED_USERNAME);

        assertAll(
                () -> assertEquals(expectedRefreshToken.getToken(), actualRefreshToken.getToken()),
                () -> verify(jwtTokenService).generateRefreshToken(EXPECTED_USERNAME),
                () -> verify(jwtTokenService).generateRefreshTokenExpirationTime(),
                () -> verify(refreshTokenRepository)
                        .updateRefreshToken(eq(EXPECTED_USERNAME), eq(expectedRefreshToken.getToken()), any())
        );
    }

    private void mockJwtRefreshTokenData(String refreshToken) {
        when(jwtTokenService.generateRefreshToken(anyString())).thenReturn(refreshToken);
        when(jwtTokenService.generateRefreshTokenExpirationTime()).thenReturn(LocalDateTime.MAX);
    }

    @Test
    void renewAccessTokenByRefreshToken_whenAccessTokenExpires_shouldRenewAccessToken() {
        var existRefreshToken = createRefreshToken(ADD_HOUR_TO_CURRENT_TIME);
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(existRefreshToken));
        when(jwtTokenService.createAccessToken(anyString(), any(Role.class))).thenReturn(EXPECTED_ACCESS_TOKEN);

        var actualAccessToken = refreshTokenService.renewAccessTokenByRefresh(existRefreshToken.getToken());

        assertAll(
                () -> assertTrue(actualAccessToken.isPresent()),
                () -> assertEquals(Optional.of(EXPECTED_ACCESS_TOKEN), actualAccessToken),
                () -> verify(refreshTokenRepository).findByToken(existRefreshToken.getToken()),
                () -> verify(jwtTokenService)
                        .createAccessToken(EXPECTED_USERNAME, existRefreshToken.getUser().getRole())
        );
    }

    @Test
    void renewAccessTokenByRefreshToken_whenAccessTokenDoesNotExpiry_shouldNotRenewAccessToken() {
        var existRefreshToken = createRefreshToken(MINUS_HOUR_FROM_CURRENT_TIME);
        when(refreshTokenRepository.findByToken(any())).thenReturn(Optional.of(existRefreshToken));

        var actualAccessToken = refreshTokenService.renewAccessTokenByRefresh(existRefreshToken.getToken());

        assertAll(
                () -> assertFalse(actualAccessToken.isPresent()),
                () -> assertTrue(actualAccessToken.isEmpty()),
                () -> verify(refreshTokenRepository).findByToken(existRefreshToken.getToken()),
                () -> verifyNoInteractions(jwtTokenService)
        );
    }

    private User createUser() {
        return new User().setUsername("user").setRole(new Role(UUID.randomUUID(), "ROLE_USER", List.of()));
    }

    private RefreshToken createRefreshToken(int hours) {
        var refreshToken = new RefreshToken().setExpiryDate(LocalDateTime.now().plusHours(hours));
        refreshToken.setUser(createUser());
        return refreshToken;
    }

    @Test
    void deleteByUsername_whenUserWithUsernameExistsInDB_shouldDeleteRefreshToken() {
        refreshTokenService.deleteByUsername(EXPECTED_USERNAME);

        verify(refreshTokenRepository).deleteByUserUsername(EXPECTED_USERNAME);
    }
}