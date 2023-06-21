package space.obminyashka.items_exchange.model.projection;

import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;

import java.util.UUID;

public interface UserAuthProjection {
    UUID getId();

    String getEmail();

    String getUsername();

    String getPassword();

    String getFirstName();

    String getLastName();

    String getLanguage();

    RefreshToken getRefreshToken();

    Role getRole();

    byte[] getAvatarImage();
}
