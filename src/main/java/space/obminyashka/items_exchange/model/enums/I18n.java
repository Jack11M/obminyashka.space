package space.obminyashka.items_exchange.model.enums;

import lombok.Getter;

@Getter
public enum I18n {
    UA("UA_uk"),
    EN("EN_en"),
    RU("RU_ru");

    private final String value;

    I18n(String value) {
        this.value = value;
    }
}
