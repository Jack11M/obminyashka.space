package space.obminyashka.items_exchange.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import space.obminyashka.items_exchange.model.enums.I18n;

import java.util.Locale;


@Component
public class StringToI18nConverter implements Converter<String, I18n> {

    @Override
    public I18n convert(String lang) {
        return switch (lang.toLowerCase(Locale.ROOT)) {
            case "ru" -> I18n.RU;
            case "ua" -> I18n.UA;
            default -> I18n.EN;
        };
    }
}