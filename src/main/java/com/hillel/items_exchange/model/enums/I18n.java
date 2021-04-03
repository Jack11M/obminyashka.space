package com.hillel.items_exchange.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum I18n {
    UA("UA_uk"),
    EN("EN_en"),
    RU("RU_ru");

    private final String value;
}
