package com.hillel.items_exchange.util;

public class PatternHandler {
    public static final String EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
    public static final String PASSWORD_MIN_7_MAX_30 = "(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=\\S+$).+.{7,30}";
    public static final String PHONE_NUMBER =
            "^\\s*(?<country>\\+?\\d{1,3})[-. (]*(?<area>\\d{3})[-. )]*(?<number>\\d{3}[-. ]*\\d{2}[-. ]*\\d{2})\\s*$";
    public static final String USERNAME_MIN_2_MAX_50 = "(?=\\S+$).{2,50}";
}
