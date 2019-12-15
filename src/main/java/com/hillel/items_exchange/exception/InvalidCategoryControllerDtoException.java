package com.hillel.items_exchange.exception;

import lombok.Getter;

@Getter
public class InvalidCategoryControllerDtoException extends RuntimeException {

    public InvalidCategoryControllerDtoException(String message) {
        super(message);
    }
}
