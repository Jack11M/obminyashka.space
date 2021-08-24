package space.obminyashka.items_exchange.security.jwt.refresh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponse {

    private static final String TOKEN_TYPE = "Bearer";
    private final String accessToken;
    private final String refreshToken;
    private final String accessTokenExpiration;
    private final String refreshTokenExpiration;
}
