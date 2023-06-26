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
import space.obminyashka.items_exchange.dto.RefreshTokenResponseDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.service.impl.AuthServiceImpl;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private static final String JWT_TOKEN = "super_secret_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String EXPECTED_USERNAME = "user";
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
    @InjectMocks
    private MessageSourceUtil messageSourceUtil;
    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void init() {
        messageSourceUtil.setMSource(messageSource);
    }

    @Test
    void finalizeAuthData_whenPositiveFlow_shouldPopulateDtoAndCreateTokenWithUsername() {
        mockJwtTokenServiceForCreationAccessAndRefreshTokens();
        var testDto = createTestDto();
        when(refreshTokenService.createRefreshToken(null, testDto.getUsername()))
                .thenReturn(new RefreshToken(REFRESH_TOKEN, LocalDateTime.MAX));

        final var actualUserLoginResponseDto = authService.finalizeAuthData(testDto);

        assertAll(
                () -> checkAccessAndRefreshTokenFieldsInDto(actualUserLoginResponseDto),
                () -> verify(jwtTokenService).createAccessToken(testDto.getUsername(), testDto.getRole()),
                () -> verify(jwtTokenService).getAccessTokenExpiration(JWT_TOKEN),
                () -> verify(refreshTokenService).createRefreshToken(null, testDto.getUsername()),
                () -> verify(jwtTokenService).getRefreshTokenExpiration(any(ZonedDateTime.class))
        );
    }

    private UserLoginResponseDto createTestDto() {
        var dto = new UserLoginResponseDto();
        dto.setEmail("user@mail.ua");
        dto.setUsername(EXPECTED_USERNAME);
        dto.setRole(new Role(UUID.randomUUID(), "ROLE_USER", List.of()));
        return dto;
    }

    private void mockJwtTokenServiceForCreationAccessAndRefreshTokens() {
        when(jwtTokenService.createAccessToken(anyString(), any(Role.class))).thenReturn(JWT_TOKEN);
        when(jwtTokenService.getAccessTokenExpiration(anyString())).thenReturn(LocalDateTime.MAX);
        when(jwtTokenService.getRefreshTokenExpiration(any(ZonedDateTime.class))).thenReturn(REFRESH_TOKEN);
    }

    @Test
    void logout_whenPositiveFlow_shouldSuccessfullyLogout() {
        authService.logout(JWT_TOKEN, EXPECTED_USERNAME);

        assertAll(
                () -> verify(jwtTokenService).invalidateAccessToken(tokenCaptor.capture()),
                () -> assertEquals(JWT_TOKEN, tokenCaptor.getValue()),
                () -> verify(refreshTokenService).deleteByUsername(usernameCaptor.capture()),
                () -> assertEquals(EXPECTED_USERNAME, usernameCaptor.getValue())
        );
    }

    @Test
    void renewAccessTokenByRefresh_whenRefreshTokenNotEmpty_shouldCreateNewAccessToken() throws RefreshTokenException {
        when(refreshTokenService.renewAccessTokenByRefresh(any())).thenReturn(Optional.of(JWT_TOKEN));

        var refreshTokenDto = authService.renewAccessTokenByRefresh(REFRESH_TOKEN);

        assertAll(
                () -> checkAccessAndRefreshTokenFieldsInDto(refreshTokenDto),
                () -> verify(refreshTokenService).renewAccessTokenByRefresh(REFRESH_TOKEN),
                () -> verify(jwtTokenService).getAccessTokenExpiration(JWT_TOKEN),
                () -> verify(jwtTokenService).getRefreshTokenExpiration(any(ZonedDateTime.class))
        );
    }

    private <T> void checkAccessAndRefreshTokenFieldsInDto(T dto) {
        assertThat(dto)
                .hasFieldOrPropertyWithValue("accessToken", JWT_TOKEN)
                .hasFieldOrPropertyWithValue("refreshToken", REFRESH_TOKEN);
    }

    @Test
    void renewAccessTokenByRefresh_whenRefreshTokenIsEmpty_shouldThrowRefreshTokenException() {
        when(refreshTokenService.renewAccessTokenByRefresh(anyString()))
                .thenAnswer(i -> Optional.of(i.getArguments()[0]));

        assertAll(
                () -> assertThrows(RefreshTokenException.class, () -> authService.renewAccessTokenByRefresh("")),
                () -> verifyNoInteractions(jwtTokenService)
        );
    }
}