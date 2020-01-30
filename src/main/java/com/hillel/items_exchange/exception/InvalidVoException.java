package com.hillel.items_exchange.exception;

import lombok.Getter;

@Getter
public class InvalidVoException extends RuntimeException {

    public InvalidVoException(String message) {
        super(message);
    }
}
