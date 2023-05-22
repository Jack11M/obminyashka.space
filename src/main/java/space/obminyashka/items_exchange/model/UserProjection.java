package space.obminyashka.items_exchange.model;

public interface UserProjection {
    String getUsername();
    String getEmail();
    Boolean getIsValidatedEmail();
    Boolean getOauth2Login();
    Role getRole();
}
