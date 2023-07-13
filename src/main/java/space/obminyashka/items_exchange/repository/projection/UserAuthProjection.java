package space.obminyashka.items_exchange.repository.projection;

import space.obminyashka.items_exchange.repository.model.RefreshToken;
import space.obminyashka.items_exchange.repository.model.Role;

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
