package com.hillel.items_exchange.exception;

import lombok.Getter;

@Getter
public class InvalidDtoException extends RuntimeException {

    public InvalidDtoException(String message) {
        super(message);
    }
}
