package space.obminyashka.items_exchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EmailValidationCodeExpiredException extends Exception {
    public EmailValidationCodeExpiredException(String message) {
        super(message);
    }
}
