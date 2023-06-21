package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.mapper.UserMapperImpl;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.projection.UserAuthProjection;
import space.obminyashka.items_exchange.service.impl.AuthServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Spy
    private UserMapperImpl userMapper;
    @Captor
    private ArgumentCaptor<String> usernameCaptor;
    @InjectMocks
    private AuthServiceImpl authService;
    private final String jwtToken = "super_secret_token";
    private final String refreshToken = "refresh_token";

    @Test
    void createUserLoginResponseDto_shouldPopulateDtoAndCreateTokenWithUsername() {
        final var projection = new UserAuthProjection(UUID.randomUUID(), "user@mail.ua", "user", "",
                "", null, null, new Role(UUID.randomUUID(), "ROLE_USER", List.of()));
        final var userForRefreshToken = new User();
        userForRefreshToken
                .setUsername(projection.getUsername())
                .setId(projection.getId());

        when(userService.findUserAuthProjectionByUsernameOrEmail(anyString())).thenReturn(Optional.of(projection));
        mockJwtTokenData();
        mockRefreshTokenData(userForRefreshToken);

        final var userLoginResponseDto = authService.createUserLoginResponseDto("user");

        assertThat(userLoginResponseDto)
                .hasFieldOrPropertyWithValue("accessToken", jwtToken)
                .hasFieldOrPropertyWithValue("refreshToken", refreshToken)
                .hasFieldOrProperty("accessTokenExpirationDate")
                .hasFieldOrProperty("refreshTokenExpirationDate");
        verify(jwtTokenService).createAccessToken(usernameCaptor.capture(), any(Role.class));
        assertEquals("user", usernameCaptor.getValue());
    }

    private void mockJwtTokenData() {
        when(jwtTokenService.createAccessToken(anyString(), any())).thenReturn(jwtToken);
        when(jwtTokenService.getAccessTokenExpiration(jwtToken)).thenReturn(LocalDateTime.MAX);
    }

    private void mockRefreshTokenData(User user) {
        when(refreshTokenService.createRefreshToken(user)).thenReturn(new RefreshToken(user, refreshToken, LocalDateTime.MAX));
        when(jwtTokenService.getRefreshTokenExpiration(any())).thenReturn(refreshToken);
    }
}