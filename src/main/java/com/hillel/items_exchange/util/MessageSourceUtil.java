package com.hillel.items_exchange.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSourceUtil {

    private static MessageSource mSource;

    @Autowired
    public void setMSource(MessageSource messageSource) {
        mSource = messageSource;
    }

    public static String getExceptionMessageSource(String message) {
        return mSource.getMessage(message,
                null,
                Locale.getDefault());
    }

    public static String getExceptionMessageSourceWithId(long id, String message) {
        return getExceptionMessageSource(message) + id;
    }

    public static String getExceptionMessageSourceWithAdditionalInfo(String message, String additionalInfo) {
        return getExceptionMessageSource(message) + additionalInfo;
    }
}
