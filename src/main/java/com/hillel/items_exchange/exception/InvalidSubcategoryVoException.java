package com.hillel.items_exchange.exception;

import lombok.Getter;

@Getter
public class InvalidSubcategoryVoException extends RuntimeException {

    public InvalidSubcategoryVoException(String message) {
        super(message);
    }
}
