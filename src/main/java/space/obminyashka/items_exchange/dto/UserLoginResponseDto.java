package space.obminyashka.items_exchange.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponseDto {

    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private String accessToken;
    private String accessTokenExpirationDate;
    private String refreshToken;
    private String refreshTokenExpirationDate;
    private byte[] avatarImage;
}
