package space.obminyashka.items_exchange.rest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import space.obminyashka.items_exchange.repository.model.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserLoginResponse {
    @JsonIgnore
    private UUID id;
    private String email;
    private String username;
    @JsonProperty("firstname")
    private String firstName;
    @JsonProperty("lastname")
    private String lastName;
    @JsonProperty(HttpHeaders.ACCEPT_LANGUAGE)
    private String language;
    @JsonIgnore
    private Role role;
    @Accessors(chain = true)
    @JsonProperty(OAuth2ParameterNames.ACCESS_TOKEN)
    private String accessToken;
    @Accessors(chain = true)
    @JsonProperty(OAuth2ParameterNames.EXPIRES_IN)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accessTokenExpirationDate;
    @Accessors(chain = true)
    @JsonProperty(OAuth2ParameterNames.REFRESH_TOKEN)
    private String refreshToken;
    @JsonProperty("refresh_expires_in")
    private String refreshTokenExpirationDate;
    private byte[] avatarImage;
}
