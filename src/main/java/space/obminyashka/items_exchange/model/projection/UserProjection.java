package space.obminyashka.items_exchange.model.projection;

import space.obminyashka.items_exchange.model.Role;

public interface UserProjection {
    String getUsername();
    String getEmail();
    Boolean getIsValidatedEmail();
    Boolean getOauth2Login();
    Role getRole();
}
