package space.obminyashka.items_exchange.security.jwt;

import io.jsonwebtoken.*;
import liquibase.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import space.obminyashka.items_exchange.model.Role;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String EMPTY_TOKEN = "";
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
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role.getName());

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtAccessTokenExpirationMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateAccessToken(String token, HttpServletRequest req) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date())
                    && !invalidatedTokensHolder.isInvalidated(token);
        } catch (JwtException e) {
            log.error("Unauthorized: {}", e.getMessage());
            req.setAttribute("detailedError", e.getMessage());
            return false;
        }
    }

    public void invalidateAccessToken(String token) {
        final Date expirationDate = getAccessTokenExpirationDate(token)
                .orElseThrow(() -> new JwtException(getMessageSource("invalid.token")));
        invalidatedTokensHolder.invalidate(token, expirationDate);
    }

    public Optional<Date> getAccessTokenExpirationDate(String token) {
        try {
            return Optional.ofNullable(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration());
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

    public String getAccessTokenExpiration(LocalDateTime localDateTime) {
        return localDateTime.plusSeconds(TimeUnit.MILLISECONDS.toSeconds(jwtAccessTokenExpirationMillis))
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String getRefreshTokenExpiration(LocalDateTime localDateTime) {
        return localDateTime.plusSeconds(jwtRefreshTokenExpirationSeconds)
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String getTokenFromHeader(HttpServletRequest request, String headerKey) {
        String bearerToken = request.getHeader(headerKey);
        final var resolvedToken = resolveToken(bearerToken);
        if (resolvedToken.isBlank()) {
            String errorMessageTokenNotStartWithBearerPrefix = getMessageSource("invalid.token");
            log.error("Unauthorized: {}", errorMessageTokenNotStartWithBearerPrefix);
            request.setAttribute("detailedError", errorMessageTokenNotStartWithBearerPrefix);
        }
        return resolvedToken;
    }

    public String resolveToken(String token) {
        if (!StringUtil.isEmpty(token)) {
            if (token.startsWith(BEARER_PREFIX)) {
                return token.substring(BEARER_PREFIX.length());
            }
            return token;
        }
        return EMPTY_TOKEN;
    }
}
