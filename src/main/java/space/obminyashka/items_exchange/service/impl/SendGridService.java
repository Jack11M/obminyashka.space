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
import space.obminyashka.items_exchange.dao.EmailConfirmationTokenRepository;
import space.obminyashka.items_exchange.exception.EmailValidationCodeExpiredException;
import space.obminyashka.items_exchange.exception.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.model.EmailConfirmationToken;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.util.EmailType;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendGridService implements MailService {

    private final EmailConfirmationTokenRepository emailRepository;
    private final SendGrid sendGrid;
    private final Email sender;

    @Value("${number.of.days.to.keep.deleted.email.confirmation.token}")
    private int numberOfDaysToKeepDeletedEmails;

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
        //TODO in ticket OBMIN-344
    }

    private Request createMailRequest(Mail mail) throws IOException {
        var request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return request;
    }

    @Override
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    public void permanentlyDeleteEmailConfirmationToken() {
        emailRepository.findAll().stream()
                .filter(this::isDurationMoreThanNumberOfDaysToKeepDeletedEmail)
                .forEach(emailRepository::delete);
    }

    private boolean isDurationMoreThanNumberOfDaysToKeepDeletedEmail(EmailConfirmationToken email) {
        Duration duration = Duration.between(email.getExpiryDate(), LocalDateTime.now());

        return duration.toDays() > numberOfDaysToKeepDeletedEmails;
    }
}
