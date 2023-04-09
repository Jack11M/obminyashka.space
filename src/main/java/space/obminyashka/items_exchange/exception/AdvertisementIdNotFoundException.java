package space.obminyashka.items_exchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdvertisementIdNotFoundException extends Exception {
    public AdvertisementIdNotFoundException(String message) {
        super(message);
    }
}
