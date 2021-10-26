package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

@Getter
@Setter
public class UserLoginResponseDto {

    private String email;
    private String username;
    private String firstname;
    private String lastname;
    @JsonProperty(OAuth2ParameterNames.ACCESS_TOKEN)
    private String accessToken;
    @JsonProperty("expires_in")
    private String accessTokenExpirationDate;
    @JsonProperty(OAuth2ParameterNames.REFRESH_TOKEN)
    private String refreshToken;
    @JsonProperty("refresh_expires_in")
    private String refreshTokenExpirationDate;
    private byte[] avatarImage;
}
