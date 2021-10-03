package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dto.RefreshTokenResponseDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.security.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.RefreshTokenService;
import space.obminyashka.items_exchange.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public Optional<UserLoginResponseDto> createUserLoginResponseDto(String username) throws UsernameNotFoundException {
        final var user = userService.findByUsernameOrEmail(username);
        if (user.isPresent()) {
            final var userLoginResponseDto = modelMapper.map(user.get(), UserLoginResponseDto.class);
            userLoginResponseDto.setAccessToken(jwtTokenProvider.createAccessToken(username, user.get().getRole()));
            userLoginResponseDto.setRefreshToken(refreshTokenService.createRefreshToken(username).getToken());
            userLoginResponseDto.setAccessTokenExpirationDate(jwtTokenProvider.getAccessTokenExpiration(LocalDateTime.now()));
            userLoginResponseDto.setRefreshTokenExpirationDate(jwtTokenProvider.getRefreshTokenExpiration(LocalDateTime.now()));
            log.info("User {} is successfully logged in", username);
            return Optional.of(userLoginResponseDto);
        }
        return Optional.empty();
    }

    @Override
    public boolean logout(String accessToken, String username) {
        final String token = jwtTokenProvider.resolveToken(accessToken);
        if (!token.isEmpty()) {
            jwtTokenProvider.invalidateAccessToken(token);
            refreshTokenService.deleteByUsername(username);
            return true;
        }
        return false;
    }

    @Override
    public RefreshTokenResponseDto renewAccessTokenByRefresh(String refreshToken) throws RefreshTokenException {
        final var token = jwtTokenProvider.resolveToken(refreshToken);
        return refreshTokenService.renewAccessTokenByRefresh(token)
                .filter(Predicate.not(String::isEmpty))
                .map(accessToken -> new RefreshTokenResponseDto(accessToken, refreshToken,
                        jwtTokenProvider.getAccessTokenExpiration(LocalDateTime.now()),
                        jwtTokenProvider.getRefreshTokenExpiration(LocalDateTime.now())))
                .orElseThrow(() -> new RefreshTokenException(getParametrizedMessageSource("refresh.token.invalid", refreshToken)));
    }
}
