package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.mapper.UserMapper;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.impl.AuthServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
    @Mock
    private UserMapper userMapper;
    @Captor
    private ArgumentCaptor<String> usernameCaptor;
    @InjectMocks
    private AuthServiceImpl authService;
    private final String jwtToken = "super_secret_token";
    private final String refreshToken = "refresh_token";

    @Test
    void createUserLoginResponseDto_shouldPopulateDtoAndCreateTokenWithUsername() {
        final var user = new User()
                .setUsername("user")
                .setEmail("user@mail.ua")
                .setRole(new Role(UUID.randomUUID(), "ROLE_USER", List.of()));

        when(userService.findUserByUsernameOrEmailFormUserAuthProjection(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toLoginResponseDto(user)).thenReturn(new UserLoginResponseDto());
        mockJwtTokenData();
        mockRefreshTokenData(user);

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