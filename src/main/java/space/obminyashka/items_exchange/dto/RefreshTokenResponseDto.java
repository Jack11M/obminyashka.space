package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.time.LocalDateTime;

public record RefreshTokenResponseDto(@JsonProperty(OAuth2ParameterNames.ACCESS_TOKEN) String accessToken,
                                      @JsonProperty(OAuth2ParameterNames.REFRESH_TOKEN) String refreshToken,
                                      @JsonProperty(OAuth2ParameterNames.EXPIRES_IN)
                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime accessTokenExpiration,
                                      @JsonProperty("refresh_expires_in") String refreshTokenExpiration) {
}
