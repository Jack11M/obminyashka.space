package space.obminyashka.items_exchange.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.PositiveMessage.*;

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
        var mail2send = new Mail();
        mail2send.setFrom(sender);
        mail2send.setTemplateId(subject.template);

        final var personalization = populatePersonalization(emailTo);
        mail2send.addPersonalization(personalization);

        var request = createMailRequest(mail2send);
        final var response = sendGrid.api(request);
        final var statusCode = response.getStatusCode();
        log.debug("[SendGridService] A sent email result. STATUS: {} BODY: {}", statusCode, response.getBody());
    }

    private static Personalization populatePersonalization(String receiver) {
        final var personalization = new Personalization();
        personalization.addDynamicTemplateData("subject", getMessageSource(EMAIL_REGISTRATION_TOPIC));
        personalization.addDynamicTemplateData("header", getMessageSource(EMAIL_REGISTRATION_HEADER));
        personalization.addDynamicTemplateData("greetings", getMessageSource(EMAIL_REGISTRATION_GREETINGS));
        personalization.addDynamicTemplateData("information", getMessageSource(EMAIL_REGISTRATION_INFORMATION));
        personalization.addDynamicTemplateData("benefits", getMessageSource(EMAIL_REGISTRATION_BENEFITS));
        personalization.addDynamicTemplateData("confirm", getMessageSource(EMAIL_REGISTRATION_CONFIRM_BUTTON));
        personalization.addDynamicTemplateData("footer", getMessageSource(EMAIL_REGISTRATION_FOOTER));
        personalization.addDynamicTemplateData("url", "https://obminyashka.space/");
        personalization.addTo(new Email(receiver));
        return personalization;
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
