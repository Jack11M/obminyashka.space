package space.obminyashka.items_exchange.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendGridService implements MailService {

    private final EmailConfirmationCodeRepository emailRepository;
    private final SendGrid sendGrid;
    private final Email sender;
    private final UserRepository userRepository;

    @Value("${number.of.days.to.keep.deleted.email.confirmation.token}")
    private int numberOfDaysToKeepDeletedEmails;

    @Override
    public void sendMail(String emailTo, EmailType subject, Locale locale, UUID codeId, String host) throws IOException {
        Email to = new Email(emailTo);
        var content = new Content(MediaType.TEXT_PLAIN_VALUE, getMessageSource(subject.body));
        var mail2send = new Mail(sender, getMessageSource(subject.header), to, content);
        var request = createMailRequest(mail2send, codeId, host);
        final var response = sendGrid.api(request);
        log.debug("[SendGridService] A sent email result. STATUS: {} BODY: {}", response.getStatusCode(), response.getBody());
    }

    @Override
    public void validateEmail(UUID validationCode) throws EmailValidationCodeNotFoundException {
        emailRepository.findById(validationCode)
                .filter(emailConfirmationCode -> LocalDateTime.now().isBefore(emailConfirmationCode.getExpiryDate()))
                .map(EmailConfirmationCode::getId)
                .ifPresentOrElse(userRepository::setValidatedEmailToUserByEmailId, this::throwNotFoundException);
    }

    private void throwNotFoundException() throws EmailValidationCodeNotFoundException {
        throw new EmailValidationCodeNotFoundException(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_NOT_FOUND_OR_EXPIRED));
    }

    private Request createMailRequest(Mail mail, UUID codeId, String host) throws IOException {
        var request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint(host.concat(EMAIL_VALIDATE_CODE.replace("{code}", codeId.toString())));
        request.setBody(mail.build());
        return request;
    }

    @Override
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    public void permanentlyDeleteEmailConfirmationCode() {
        emailRepository.findAll().stream()
                .filter(this::isDurationMoreThanNumberOfDaysToKeepDeletedEmail)
                .forEach(emailRepository::delete);
    }

    private boolean isDurationMoreThanNumberOfDaysToKeepDeletedEmail(EmailConfirmationCode email) {
        Duration duration = Duration.between(email.getExpiryDate(), LocalDateTime.now());

        return duration.toDays() > numberOfDaysToKeepDeletedEmails;
    }




}
