package com.hillel.items_exchange.model.enums;

import lombok.Getter;

@Getter
public enum Lang {
    UA("ua"),
    EN("en"),
    RU("ru");

    private final String value;

    Lang(String value) {
        this.value = value;
    }
}
