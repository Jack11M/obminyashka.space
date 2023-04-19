package space.obminyashka.items_exchange.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.RefreshTokenRepository;
import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {
        final String token = jwtTokenService.generateRefreshToken(user.getUsername());
        final var tokenExpirationTime = jwtTokenService.generateRefreshTokenExpirationTime();
        return refreshTokenRepository.save(Optional.ofNullable(user.getRefreshToken())
                .map(refreshToken -> refreshToken.setToken(token)
                        .setCreated(LocalDateTime.now())
                        .setExpiryDate(tokenExpirationTime))
                .orElseGet(() -> new RefreshToken(user, token, tokenExpirationTime)));
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
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
        return refreshTokenRepository.findByToken(refreshToken)
                .filter(Predicate.not(this::isRefreshTokenExpired))
                .map(RefreshToken::getUser)
                .map(user -> jwtTokenService.createAccessToken(user.getUsername(), user.getRole()));
    }
}
