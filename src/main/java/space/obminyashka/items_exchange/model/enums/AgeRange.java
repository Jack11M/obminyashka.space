package space.obminyashka.items_exchange.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.*;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;


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

    public static AgeRange fromValue(String value) {
        return Arrays.stream(AgeRange.values())
                .filter(ageRange -> ageRange.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(getMessageSource(INVALID_ENUM_VALUE)));
    }
}
