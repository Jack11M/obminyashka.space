package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import static space.obminyashka.items_exchange.config.SecurityConfig.ACCESS_TOKEN;
import static space.obminyashka.items_exchange.config.SecurityConfig.REFRESH_TOKEN;

public record RefreshTokenResponseDto(@JsonProperty(ACCESS_TOKEN) String accessToken,
                                      @JsonProperty(REFRESH_TOKEN) String refreshToken,
                                      @JsonProperty("expires_in") String accessTokenExpiration,
                                      @JsonProperty("refresh_expires_in") String refreshTokenExpiration) {
}
