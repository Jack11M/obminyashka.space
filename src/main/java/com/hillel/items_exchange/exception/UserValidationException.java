package com.hillel.items_exchange.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserValidationException extends RuntimeException {

    public UserValidationException(String message) {
        super(message);
    }
}
