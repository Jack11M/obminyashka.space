package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.dao.RefreshTokenRepository;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.impl.RefreshTokenServiceImpl;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @ParameterizedTest
    @MethodSource("testUsersWithRefreshTokens")
    void createRefreshToken_shouldCreateOrUpdateToken(User testUser) {
        when(jwtTokenService.generateRefreshToken(anyString())).thenReturn("token_string");
        when(jwtTokenService.generateRefreshTokenExpirationTime()).thenReturn(LocalDateTime.MAX);
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final var refreshToken = refreshTokenService.createRefreshToken(testUser);

        assertAll(
                () -> assertNotNull(refreshToken),
                () -> assertNotNull(refreshToken.getToken()),
                () -> assertNotNull(refreshToken.getExpiryDate()),
                () -> verify(refreshTokenRepository).save(refreshToken)
        );
    }

    private static Stream<User> testUsersWithRefreshTokens() {
        return Stream.of(
                new User().setUsername("user_without_token"),
                new User().setUsername("user_with_token").setRefreshToken(new RefreshToken()));
    }
}