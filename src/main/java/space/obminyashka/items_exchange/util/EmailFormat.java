package space.obminyashka.items_exchange.util;


import org.apache.commons.validator.routines.EmailValidator;

public class EmailFormat {

    public static boolean isEmailValidFormat(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
