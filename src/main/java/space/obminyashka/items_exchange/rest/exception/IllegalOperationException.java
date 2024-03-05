package space.obminyashka.items_exchange.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalOperationException extends Exception {

    public IllegalOperationException(String message) {
        super(message);
    }
}
