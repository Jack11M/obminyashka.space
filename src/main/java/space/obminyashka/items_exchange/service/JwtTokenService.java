package space.obminyashka.items_exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.authorization.jwt.InvalidatedTokensHolder;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService implements OAuth2TokenValidator<Jwt> {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final InvalidatedTokensHolder invalidatedTokensHolder;
    private final JwtEncoder tokenEncoder;
    private final JwtDecoder tokenDecoder;

    @Value("${app.access.jwt.expiration.time.ms}")
    private long jwtAccessTokenExpirationMillis;
    @Value("${app.refresh.jwt.expiration.time.seconds}")
    private long jwtRefreshTokenExpirationSeconds;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        final var isTokenValid = Instant.now().isBefore(token.getExpiresAt())
                && !invalidatedTokensHolder.isInvalidated(token.getTokenValue());
        return isTokenValid
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("Token is expired"));
    }

    public String createAccessToken(String username, Role role) {
        final var now = Instant.now();
        final var expiresAt = now.plusMillis(jwtAccessTokenExpirationMillis);
        final var claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(username)
                .claim("role", role.getName())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();
        return this.tokenEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(() -> "HS256").build(), claims))
                .getTokenValue();
    }

    public void invalidateAccessToken(String token) {
        final Instant expirationDate = getAccessTokenExpirationDate(token)
                .orElseThrow(() -> new JwtException(getMessageSource(
                        ResponseMessagesHandler.ValidationMessage.INVALID_TOKEN)));
        invalidatedTokensHolder.invalidate(token, Date.from(expirationDate));
    }

    public Optional<Instant> getAccessTokenExpirationDate(String token) {
        try {
            return Optional.ofNullable(tokenDecoder.decode(token).getExpiresAt());
        } catch (JwtException exception) {
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

    public LocalDateTime getAccessTokenExpiration(String accessToken) {
        return tokenDecoder.decode(accessToken).getExpiresAt().atZone(ZoneId.of("Europe/Kiev")).toLocalDateTime();
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
