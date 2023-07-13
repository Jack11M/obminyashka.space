package space.obminyashka.items_exchange.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataConflictException extends Exception {
    public DataConflictException(String message) {
        super(message);
    }
}
