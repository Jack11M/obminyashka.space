package com.hillel.items_exchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class IllegalOperationException extends RuntimeException {

    public IllegalOperationException(String message) {
        super(message);
    }
}
