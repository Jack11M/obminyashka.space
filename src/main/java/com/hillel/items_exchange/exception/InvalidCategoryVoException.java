package com.hillel.items_exchange.exception;

import lombok.Getter;

@Getter
public class InvalidCategoryVoException extends RuntimeException {

    public InvalidCategoryVoException(String message) {
        super(message);
    }
}
