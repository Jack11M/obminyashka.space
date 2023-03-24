package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
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
     * Validate users' email by previously generated UUID code
     * @param validationCode a code for an email activation
     * @throws EmailValidationCodeNotFoundException when the code isn't found
     * @throws EmailValidationCodeExpiredException when the code is expired
     */
    void validateEmail(UUID validationCode) throws EmailValidationCodeNotFoundException, EmailValidationCodeExpiredException;
}
