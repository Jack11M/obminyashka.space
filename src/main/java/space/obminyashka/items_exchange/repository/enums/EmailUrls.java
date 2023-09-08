package space.obminyashka.items_exchange.repository.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static space.obminyashka.items_exchange.rest.api.ApiKey.EMAIL_VALIDATE_CODE;
import static space.obminyashka.items_exchange.rest.api.ApiKey.USER_SERVICE_PASSWORD_CONFIRM;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_ENUM_VALUE;

@Slf4j
@RequiredArgsConstructor
public enum EmailUrls {

    REGISTRATION(EMAIL_VALIDATE_CODE),
    CHANGING(EMAIL_VALIDATE_CODE),
    RESET(USER_SERVICE_PASSWORD_CONFIRM);

    @JsonValue
    @Getter
    private final String value;

    @JsonCreator
    public static EmailUrls fromValue(String value) {
        try {
            return EmailUrls.valueOf(value);
        } catch (IllegalArgumentException e) {
            log.info("Received non-enum value for EmailUrls: {}", value);
            return Arrays.stream(EmailUrls.values())
                    .filter(emailUrls -> emailUrls.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(getMessageSource(INVALID_ENUM_VALUE)));
        }
    }

}
