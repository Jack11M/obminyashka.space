package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.exception.EmailTokenExpiredException;
import space.obminyashka.items_exchange.exception.EmailTokenNotExistsException;
import space.obminyashka.items_exchange.util.EmailType;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public interface MailService {

    /**
     * Send a simple email to an end-user with provided locale
     * @param emailTo a receiver email address
     * @param emailType one of the supported email types to be sent
     * @param locale user locale
     * @throws IOException when service is unavailable or some unexpected case happened
     */
    void sendMail(String emailTo, EmailType emailType, Locale locale) throws IOException;

    /**
     * Validate code and if it is existed and unexpired, users`s email will be confirmed
     * @param validationCode a code for activate email
     * @throws EmailTokenNotExistsException when service can`t find code in database
     * @throws EmailTokenExpiredException when service find code but code is expired
     */
    void validateEmail(UUID validationCode) throws EmailTokenNotExistsException, EmailTokenExpiredException;
}
