package space.obminyashka.items_exchange.model.projection;

import space.obminyashka.items_exchange.model.Role;

/**
 * The UserProjection interface provides a projection of a user with a limited set of fields.
 * Projections allow you to define interfaces or classes that specify a subset of attributes or properties of an entity,
 * allowing you to retrieve only the required data from a query instead of fetching the entire entity.
 */
public interface UserProjection {
    String getUsername();
    Role getRole();
}
