package com.hillel.evoApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum City {
    VINNYTSIA("Винница"), DNIPRO("Днепр"), DONETSK("Донецк"), ZHYTOMYR("Житомир"),
    ZAPORIZHIA("Запорожье"), IVANO_FRANKIVSK("Ивано-Франковск"), KYIV("Киев"),
    KROPYVNYTSKYI‎("Кропивницкий"), LUHANSK("Луганск"), LUTSK("Луцк"), LVIV("Львов"),
    MYKOLAIV‎("Николаев"), ODESSA‎("Одесса"), POLTAVA‎("Полтава"), RIVNE‎("Ровно"),
    SUMY‎("Сумы"), TERNOPIL‎("Тернополь"), UZHHOROD("Ужгород"), KHARKIV("Харьков"),
    KHERSON("Херсон"), KHMELNYTSKYI("Хмельницкий"), CHERKASY("Черкассы"),
    CHERNIHIV("Чернигов"), CHERNIVTSI("Черновцы"), AR_CRIMEA("АР Крым");

    private String russianValue;

    City(String russianValue) {
        this.russianValue = russianValue;
    }
}