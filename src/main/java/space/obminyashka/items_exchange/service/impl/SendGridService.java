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
import java.util.UUID;

import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.RegistrationMessage.*;

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
    public void sendMail(String emailTo, EmailType subject, UUID codeId, String host) throws IOException {
        var mail2send = new Mail();
        mail2send.setFrom(sender);
        mail2send.setTemplateId(subject.template);

        final var personalization = populatePersonalization(subject.topic, codeId, host);
        personalization.addTo(new Email(emailTo));
        mail2send.addPersonalization(personalization);

        var request = createMailRequest(mail2send);
        final var response = sendGrid.api(request);
        final var statusCode = response.getStatusCode();
        log.debug("[SendGridService] A sent email result. STATUS: {} BODY: {}", statusCode, response.getBody());
    }

    private static Personalization populatePersonalization(String subject, UUID codeId, String host) {
        final var personalization = new Personalization();
        personalization.addDynamicTemplateData("subject", getMessageSource(subject));
        personalization.addDynamicTemplateData("header", getMessageSource(EMAIL_HEADER));
        personalization.addDynamicTemplateData("greetings", getMessageSource(EMAIL_GREETINGS));
        personalization.addDynamicTemplateData("information", getMessageSource(EMAIL_INFORMATION));
        personalization.addDynamicTemplateData("benefits", getMessageSource(EMAIL_BENEFITS));
        personalization.addDynamicTemplateData("confirm", getMessageSource(EMAIL_BUTTON));
        personalization.addDynamicTemplateData("footer", getMessageSource(EMAIL_FOOTER));
        personalization.addDynamicTemplateData("url", host.concat(EMAIL_VALIDATE_CODE.replace("{code}", codeId.toString())));
        return personalization;
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

    private Request createMailRequest(Mail mail) throws IOException {
        var request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
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
