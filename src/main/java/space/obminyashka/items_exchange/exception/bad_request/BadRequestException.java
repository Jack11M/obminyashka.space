package space.obminyashka.items_exchange.exception.bad_request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }
}
