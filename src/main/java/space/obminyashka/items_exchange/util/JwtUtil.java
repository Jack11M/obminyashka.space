package space.obminyashka.items_exchange.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${app.access.jwt.expiration.time.ms}")
    private static long jwtAccessTokenExpirationMillis;

    @Value("${app.refresh.jwt.expiration.time.seconds}")
    private static long jwtRefreshTokenExpirationSeconds;

    public static String getAccessTokenExpiration(LocalDateTime localDateTime) {
        return localDateTime.plusSeconds(TimeUnit.MILLISECONDS.toSeconds(jwtAccessTokenExpirationMillis))
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String getRefreshTokenExpiration(LocalDateTime localDateTime) {
        return localDateTime.plusSeconds(jwtRefreshTokenExpirationSeconds)
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
