package space.obminyashka.items_exchange.rest.response.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSourceProxy {

    private static MessageSource mSource;

    @Autowired
    public void setMSource(MessageSource messageSource) {
        mSource = messageSource;
    }

    public static String getMessageSource(String message) {
        return getParametrizedMessageSource(message, (Object[]) null);
    }

    public static String getParametrizedMessageSource(String message, Object... parameters) {
        return mSource.getMessage(message,
                parameters,
                LocaleContextHolder.getLocale());
    }

    public static String getExceptionMessageSourceWithId(long id, String message) {
        return getMessageSource(message) + id;
    }

    public static String getExceptionMessageSourceWithAdditionalInfo(String message, String additionalInfo) {
        return getMessageSource(message) + additionalInfo;
    }
}
