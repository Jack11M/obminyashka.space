package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dto.RefreshTokenResponseDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.RefreshTokenService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String TIMEZONE_KIEV = "Europe/Kiev";

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public Optional<UserLoginResponseDto> createUserLoginResponseDto(String username) throws UsernameNotFoundException {
        final var user = userService.findByUsernameOrEmail(username);
        if (user.isPresent()) {
            final var userLoginResponseDto = modelMapper.map(user.get(), UserLoginResponseDto.class);
            userLoginResponseDto.setAccessToken(jwtTokenService.createAccessToken(username, user.get().getRole()));
            userLoginResponseDto.setRefreshToken(refreshTokenService.createRefreshToken(user.get()).getToken());
            userLoginResponseDto.setAccessTokenExpirationDate(jwtTokenService.getAccessTokenExpiration(ZonedDateTime.now(ZoneId.of(TIMEZONE_KIEV))));
            userLoginResponseDto.setRefreshTokenExpirationDate(jwtTokenService.getRefreshTokenExpiration(ZonedDateTime.now(ZoneId.of(TIMEZONE_KIEV))));
            log.info("User {} is successfully logged in", username);
            return Optional.of(userLoginResponseDto);
        }
        return Optional.empty();
    }

    @Override
    public boolean logout(String accessToken, String username) {
        final String token = JwtTokenService.resolveToken(accessToken);
        if (!token.isEmpty()) {
            jwtTokenService.invalidateAccessToken(token);
            refreshTokenService.deleteByUsername(username);
            return true;
        }
        return false;
    }

    @Override
    public RefreshTokenResponseDto renewAccessTokenByRefresh(String refreshToken) throws RefreshTokenException {
        return refreshTokenService.renewAccessTokenByRefresh(refreshToken)
                .filter(Predicate.not(String::isEmpty))
                .map(accessToken -> new RefreshTokenResponseDto(accessToken, refreshToken,
                        jwtTokenService.getAccessTokenExpiration(ZonedDateTime.now(ZoneId.of(TIMEZONE_KIEV))),
                        jwtTokenService.getRefreshTokenExpiration(ZonedDateTime.now(ZoneId.of(TIMEZONE_KIEV)))))
                .orElseThrow(() -> new RefreshTokenException(getParametrizedMessageSource(
                        ResponseMessagesHandler.ValidationMessage.INVALID_REFRESH_TOKEN, refreshToken)));
    }
}
