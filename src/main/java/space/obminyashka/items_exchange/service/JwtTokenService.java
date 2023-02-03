package space.obminyashka.items_exchange.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.authorization.jwt.InvalidatedTokensHolder;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final UserDetailsService userDetailsService;
    private final InvalidatedTokensHolder invalidatedTokensHolder;

    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.access.jwt.expiration.time.ms}")
    private long jwtAccessTokenExpirationMillis;
    @Value("${app.refresh.jwt.expiration.time.seconds}")
    private long jwtRefreshTokenExpirationSeconds;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createAccessToken(String username, Role role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role.getName())
                .withExpiresAt(Instant.now().plusMillis(jwtAccessTokenExpirationMillis))
                .sign(Algorithm.HMAC512(secret));
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getVerify(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT getVerify(String token) {
        return JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
    }

    public boolean validateAccessToken(String token) throws AccessDeniedException {
        try {
            return Instant.now().isBefore(getVerify(token).getExpiresAtAsInstant())
                    && !invalidatedTokensHolder.isInvalidated(token);
        } catch (JWTVerificationException e) {
            log.error("Unauthorized: {}", e.getMessage());
            return false;
        }
    }

    public void invalidateAccessToken(String token) {
        final Date expirationDate = getAccessTokenExpirationDate(token)
                .orElseThrow(() -> new JWTVerificationException(getMessageSource(
                        ResponseMessagesHandler.ValidationMessage.INVALID_TOKEN)));
        invalidatedTokensHolder.invalidate(token, expirationDate);
    }

    public Optional<Date> getAccessTokenExpirationDate(String token) {
        try {
            return Optional.ofNullable(getVerify(token).getExpiresAt());
        } catch (JWTVerificationException exception) {
            log.error("Token parsing error {}", exception.getMessage());
            return Optional.empty();
        }
    }

    public String generateRefreshToken(String username) {
        return String.format("%s%s", username, UUID.randomUUID());
    }

    public LocalDateTime generateRefreshTokenExpirationTime() {
        return LocalDateTime.now().plusSeconds(jwtRefreshTokenExpirationSeconds);
    }

    public String getAccessTokenExpiration(ZonedDateTime zonedDateTime) {
        return zonedDateTime.plusSeconds(TimeUnit.MILLISECONDS.toSeconds(jwtAccessTokenExpirationMillis))
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String getRefreshTokenExpiration(ZonedDateTime zonedDateTime) {
        return zonedDateTime.plusSeconds(jwtRefreshTokenExpirationSeconds)
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String resolveToken(String token) {
        return Optional.ofNullable(token)
                .filter(it -> it.startsWith(BEARER_PREFIX))
                .map(it -> it.substring(BEARER_PREFIX.length()))
                .orElse(token);
    }
}
