package space.obminyashka.items_exchange.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedMediaTypeException extends Exception {

    public UnsupportedMediaTypeException(String message) {
        super(message);
    }
}
