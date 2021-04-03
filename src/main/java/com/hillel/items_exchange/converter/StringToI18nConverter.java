package com.hillel.items_exchange.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.hillel.items_exchange.model.enums.I18n;


@Component
public class StringToI18nConverter implements Converter<String, I18n> {

    @Override
    public I18n convert(String lang) {
        switch (lang) {
            case "ru":
                return I18n.RU;
            case "ua":
                return I18n.UA;
            default:
                return I18n.EN;
        }
    }
}