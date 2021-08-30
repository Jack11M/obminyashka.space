package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.security.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.RefreshTokenService;
import space.obminyashka.items_exchange.service.UserService;

import java.time.LocalDateTime;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserLoginResponseDto createUserLoginResponseDto(String username) throws UsernameNotFoundException {
        final var user = userService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(getMessageSource("exception.user.not-found")));
        final var userLoginResponseDto = modelMapper.map(user, UserLoginResponseDto.class);
        userLoginResponseDto.setAccessToken(jwtTokenProvider.createAccessToken(username, user.getRole()));
        userLoginResponseDto.setRefreshToken(refreshTokenService.createRefreshToken(username).getToken());
        userLoginResponseDto.setAccessTokenExpirationDate(jwtTokenProvider.getAccessTokenExpiration(LocalDateTime.now()));
        userLoginResponseDto.setRefreshTokenExpirationDate(jwtTokenProvider.getRefreshTokenExpiration(LocalDateTime.now()));
        return userLoginResponseDto;
    }
}
