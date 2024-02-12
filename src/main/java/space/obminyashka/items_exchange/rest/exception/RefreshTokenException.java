package space.obminyashka.items_exchange.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshTokenException extends Exception {

    public RefreshTokenException(String message) {
        super(message);
    }
}
