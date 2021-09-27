package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static space.obminyashka.items_exchange.config.SecurityConfig.ACCESS_TOKEN;
import static space.obminyashka.items_exchange.config.SecurityConfig.REFRESH_TOKEN;

@Getter
@Setter
public class UserLoginResponseDto {

    private String email;
    private String username;
    private String firstname;
    private String lastname;
    @JsonProperty(ACCESS_TOKEN)
    private String accessToken;
    @JsonProperty("expires_in")
    private String accessTokenExpirationDate;
    @JsonProperty(REFRESH_TOKEN)
    private String refreshToken;
    @JsonProperty("refresh_expires_in")
    private String refreshTokenExpirationDate;
    private byte[] avatarImage;
}
