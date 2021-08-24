package space.obminyashka.items_exchange.security.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${app.refresh.jwt.expiration.time.seconds}")
    private long jwtRefreshTokenExpirationSeconds;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String username) {
        final var user = userService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(getMessageSource("exception.user.not-found")));
        return refreshTokenRepository.save(new RefreshToken(user, generateToken(username), getRefreshTokenExpiryDate()));
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

    private String generateToken(String username) {
        return String.format("%s%s%s", username, UUID.randomUUID(), UUID.randomUUID());
    }

    private LocalDateTime getRefreshTokenExpiryDate() {
        return LocalDateTime.now().plusSeconds(jwtRefreshTokenExpirationSeconds);
    }
}
