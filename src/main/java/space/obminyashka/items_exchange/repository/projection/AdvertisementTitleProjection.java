package space.obminyashka.items_exchange.repository.projection;

import space.obminyashka.items_exchange.repository.model.Location;

import java.util.UUID;

public interface AdvertisementTitleProjection {
    UUID getId();
    byte[] getDefaultPhoto();
    UUID getUserId();
    String getTopic();
    Location getLocation();
}
