package com.hillel.items_exchange.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternHandler {

    public static final String EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
    public static final String PASSWORD_MIN_7_MAX_30 =
            "(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=\\S+$)[A-Za-z0-9\\W_]{7,30}";
    public static final String PHONE_NUMBER =
            "^\\s*(?<country>\\+?\\d{2})[-. (]*(?<area>\\d{3})[-. )]*(?<number>\\d{3}[-. ]*\\d{2}[-. ]*\\d{2})\\s*$";
    public static final String USERNAME_MIN_2_MAX_50 = "(?=\\S+$)[A-Za-z0-9А-Яа-я\\W_]{2,50}";
    public static final String WORD_EMPTY_OR_MIN_2_MAX_50 = "^$|^[\\w-']{2,50}+$";
}