package space.obminyashka.items_exchange.exception.not_found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdvertisementIdNotFoundException extends RuntimeException {

    public AdvertisementIdNotFoundException(String message) {
        super(message);
    }
}
