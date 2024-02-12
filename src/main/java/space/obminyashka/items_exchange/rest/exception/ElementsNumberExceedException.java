package space.obminyashka.items_exchange.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ElementsNumberExceedException extends Exception {
    public ElementsNumberExceedException(String message) {
        super(message);
    }
}
