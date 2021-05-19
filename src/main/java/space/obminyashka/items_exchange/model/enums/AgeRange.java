package space.obminyashka.items_exchange.model.enums;

import lombok.Getter;

@Getter
public enum AgeRange {
    YOUNGER_THAN_1("0-1"),
    FROM_1_TO_2("1-2"),
    FROM_3_TO_5("3-5"),
    FROM_6_TO_9("6-9"),
    FROM_10_TO_12("10-12"),
    FROM_12_TO_14("12-14"),
    OLDER_THAN_14("14+");

    private final String value;

    AgeRange(String value) {
        this.value = value;
    }
}
