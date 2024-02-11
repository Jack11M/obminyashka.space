package space.obminyashka.items_exchange.rest.exception.not_found;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityIdNotFoundException extends RuntimeException {

    public EntityIdNotFoundException(String message) {
        super(message);
    }
}