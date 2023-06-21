package space.obminyashka.items_exchange.model.projection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.model.Role;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Getter
public class UserAuthProjection {
    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private Locale language;
    @Setter
    private String accessToken;
    @Setter
    private LocalDateTime accessTokenExpirationDate;
    @Setter
    private String refreshToken;
    @Setter
    private String refreshTokenExpirationDate;
    private byte[] avatarImage;
    private Role role;

    public UserAuthProjection(UUID id, String email, String username, String firstName,
                              String lastName, Locale language, byte[] avatarImage, Role role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
        this.avatarImage = avatarImage;
        this.role = role;
    }
}
