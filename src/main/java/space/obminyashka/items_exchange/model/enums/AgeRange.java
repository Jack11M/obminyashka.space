package space.obminyashka.items_exchange.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;


@Slf4j
@RequiredArgsConstructor
public enum AgeRange {
    YOUNGER_THAN_1("0-1"),
    FROM_1_TO_2("1-2"),
    FROM_3_TO_5("3-5"),
    FROM_6_TO_9("6-9"),
    FROM_10_TO_12("10-12"),
    FROM_12_TO_14("12-14"),
    OLDER_THAN_14("14+");

    @JsonValue
    @Getter
    private final String value;

    @JsonCreator
    public static AgeRange fromValue(String value) {
        try {
            return AgeRange.valueOf(value);
        } catch (IllegalArgumentException e) {
            log.info("Received non-enum value for AgeRange: {}", value);
            return Arrays.stream(AgeRange.values())
                    .filter(ageRange -> ageRange.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(getMessageSource(INVALID_ENUM_VALUE)));
        }
    }
}
