package space.obminyashka.items_exchange.model.projection;

import space.obminyashka.items_exchange.model.Advertisement;

import java.util.List;

public interface UserFavoriteAdvProjection {
    List<Advertisement> getFavoriteAdvertisements();
}
