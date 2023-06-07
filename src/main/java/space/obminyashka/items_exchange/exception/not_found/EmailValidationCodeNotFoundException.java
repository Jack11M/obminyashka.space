package space.obminyashka.items_exchange.exception.not_found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailValidationCodeNotFoundException extends RuntimeException {
    public EmailValidationCodeNotFoundException(String message) {
        super(message);
    }
}
