package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.rest.response.RefreshTokenResponse;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;
import space.obminyashka.items_exchange.rest.exception.RefreshTokenException;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.RefreshTokenService;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String TIMEZONE_KIEV = "Europe/Kiev";

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    public UserLoginResponse finalizeAuthData(UserLoginResponse userDto) {
        userDto.setAccessToken(jwtTokenService.createAccessToken(userDto.getUsername(), userDto.getRole()))
                .setAccessTokenExpirationDate(jwtTokenService.getAccessTokenExpiration(userDto.getAccessToken()))
                .setRefreshToken(refreshTokenService
                        .createRefreshToken(userDto.getRefreshToken(), userDto.getUsername()).getToken())
                .setRefreshTokenExpirationDate(jwtTokenService
                        .getRefreshTokenExpiration(ZonedDateTime.now(ZoneId.of(TIMEZONE_KIEV))));

        log.info("[AuthServiceImpl] User '{}' is successfully logged in", userDto.getId());

        return userDto;
    }

    @Override
    public void logout(String accessToken, String username) {
        final String token = JwtTokenService.resolveToken(accessToken);
        jwtTokenService.invalidateAccessToken(token);
        refreshTokenService.deleteByUsername(username);
    }

    @Override
    public RefreshTokenResponse renewAccessTokenByRefresh(String refreshToken) throws RefreshTokenException {
        return refreshTokenService.renewAccessTokenByRefresh(refreshToken)
                .filter(Predicate.not(String::isEmpty))
                .map(accessToken -> new RefreshTokenResponse(accessToken, refreshToken,
                        jwtTokenService.getAccessTokenExpiration(accessToken),
                        jwtTokenService.getRefreshTokenExpiration(ZonedDateTime.now(ZoneId.of(TIMEZONE_KIEV)))))
                .orElseThrow(() -> new RefreshTokenException(getParametrizedMessageSource(
                        ResponseMessagesHandler.ValidationMessage.INVALID_REFRESH_TOKEN, refreshToken)));
    }
}
