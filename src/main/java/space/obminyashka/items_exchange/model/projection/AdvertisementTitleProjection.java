package space.obminyashka.items_exchange.model.projection;

import space.obminyashka.items_exchange.model.Location;

import java.util.UUID;

public interface AdvertisementTitleProjection {
    UUID getId();
    String getTopic();
    Location getLocation();
}
