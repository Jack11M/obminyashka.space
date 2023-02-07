package space.obminyashka.items_exchange.util;


import java.util.regex.Pattern;

public class EmailFormat {

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
