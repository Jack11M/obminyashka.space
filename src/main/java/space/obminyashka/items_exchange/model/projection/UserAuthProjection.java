package space.obminyashka.items_exchange.model.projection;

import space.obminyashka.items_exchange.model.RefreshToken;
import space.obminyashka.items_exchange.model.Role;

import java.util.Locale;
import java.util.UUID;

public interface UserAuthProjection {
    UUID getId();
    String getEmail();
    String getUsername();
    String getFirstName();
    String getLastName();
    Locale getLanguage();
    Role getRole();
    RefreshToken getRefreshToken();
    byte[] getAvatarImage();
}
