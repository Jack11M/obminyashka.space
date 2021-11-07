package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.authorization.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.dao.RefreshTokenRepository;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.service.RefreshTokenService;
import space.obminyashka.items_exchange.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String username) {
        final var user = userService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(getMessageSource("exception.user.not-found")));
        return refreshTokenRepository.save(new RefreshToken(user, jwtTokenProvider.generateRefreshToken(username),
                jwtTokenProvider.generateRefreshTokenExpirationTime()));
    }

    @Override
    public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }

    @Override
    public Optional<String> renewAccessTokenByRefresh(String refreshToken) {
        return findByToken(refreshToken)
                .filter(Predicate.not(this::isRefreshTokenExpired))
                .map(RefreshToken::getUser)
                .map(user -> jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole()));
    }
}
