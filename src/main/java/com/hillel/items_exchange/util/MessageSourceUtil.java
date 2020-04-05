package com.hillel.items_exchange.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageSourceUtil {

    private final MessageSource messageSource;

    public String getExceptionMessageSource(String message) {
        return messageSource.getMessage(message,
                null,
                Locale.getDefault());
    }

    public String getExceptionMessageSourceWithId(long id, String message) {
        return getExceptionMessageSource(message) + id;
    }

    public String getExceptionMessageSourceWithAdditionalInfo(String message, String additionalInfo) {
        return getExceptionMessageSource(message) + additionalInfo;
    }
}
