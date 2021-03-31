package com.hillel.items_exchange.converter;

import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter<E extends Enum<E>> {

    public E convert(Class<E> enumType, String source) {
        return E.valueOf(enumType, source.toUpperCase());
    }
}