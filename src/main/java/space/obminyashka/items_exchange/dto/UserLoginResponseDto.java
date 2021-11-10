package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

@Getter
@Setter
public class UserLoginResponseDto {

    private String email;
    private String username;
    @JsonProperty("firstname")
    private String firstName;
    @JsonProperty("lastname")
    private String lastName;
    @JsonProperty(HttpHeaders.ACCEPT_LANGUAGE)
    private String language;
    @JsonProperty(OAuth2ParameterNames.ACCESS_TOKEN)
    private String accessToken;
    @JsonProperty(OAuth2ParameterNames.EXPIRES_IN)
    private String accessTokenExpirationDate;
    @JsonProperty(OAuth2ParameterNames.REFRESH_TOKEN)
    private String refreshToken;
    @JsonProperty("refresh_expires_in")
    private String refreshTokenExpirationDate;
    private byte[] avatarImage;
}
