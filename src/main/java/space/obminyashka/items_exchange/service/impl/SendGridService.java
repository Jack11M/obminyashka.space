package space.obminyashka.items_exchange.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@AllArgsConstructor
@Slf4j
public class SendGridService implements MailService {
    private final SendGrid sendGrid;
    private final Email sender;
    private final UserRepository userRepository;
    private final EmailConfirmationCodeRepository emailConfirmationCodeRepository;

    @Override
    public void sendMail(String emailTo, EmailType subject, Locale locale) throws IOException {
        Email to = new Email(emailTo);
        var content = new Content(MediaType.TEXT_PLAIN_VALUE, getMessageSource(subject.body));
        var mail2send = new Mail(sender, getMessageSource(subject.header), to, content);
        var request = createMailRequest(mail2send);
        final var response = sendGrid.api(request);
        log.debug("[SendGridService] A sent email result. STATUS: {} BODY: {}", response.getStatusCode(), response.getBody());
    }

    @Override
    public void validateEmail(UUID validationCode) throws EmailValidationCodeNotFoundException, EmailValidationCodeExpiredException {
        Optional<EmailConfirmationCode> optionalEmail = emailConfirmationCodeRepository.findById(validationCode);

        optionalEmail.orElseThrow(() -> new EmailValidationCodeNotFoundException(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_NOT_FOUND)));

        if (optionalEmail.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new EmailValidationCodeExpiredException(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_CODE_EXPIRED));
        }
        userRepository.setValidatedEmailToUserByEmailId(optionalEmail.get().getId());
    }

    private Request createMailRequest(Mail mail) throws IOException {
        var request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return request;
    }
}
