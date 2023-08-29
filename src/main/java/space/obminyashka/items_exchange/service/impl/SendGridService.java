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
import space.obminyashka.items_exchange.repository.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.repository.UserRepository;
import space.obminyashka.items_exchange.rest.exception.EmailSendingException;
import space.obminyashka.items_exchange.rest.exception.not_found.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.repository.model.EmailConfirmationCode;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.util.EmailType;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendGridService implements MailService {

    public static final Map<String, String> EMAIL_TEMPLATE_KEYS = Map.of(
        "subject", "topic",
            "header", "email.header",
            "greetings", "email.greetings",
            "information", "email.action.information",
            "benefits", "email.benefits",
            "confirm", "email.confirm.button",
            "footer", "email.footer"
    );
    private final EmailConfirmationCodeRepository emailRepository;
    private final SendGrid sendGrid;
    private final Email sender;
    private final UserRepository userRepository;

    @Value("${number.of.days.to.keep.deleted.email.confirmation.token}")
    private int numberOfDaysToKeepDeletedEmails;

    @Override
    public UUID sendEmailTemplateAndGenerateConfrimationCode(String emailTo, EmailType emailType, String host){
        var mail2send = new Mail();
        mail2send.setFrom(sender);
        mail2send.setTemplateId(emailType.template);

        UUID codeId = UUID.randomUUID();
        final var personalization = createPersonalizationAndSetParameters(emailType, codeId, host);
        personalization.addTo(new Email(emailTo));
        mail2send.addPersonalization(personalization);

        createRequest(mail2send, emailType);

        return codeId;
    }

    private void createRequest(Mail mail2send, EmailType emailType) {
        try {
            Request request  = createMailRequest(mail2send);
            final var response = sendGrid.api(request);
            final var statusCode = response.getStatusCode();

            log.debug("[SendGridService] A sent email result. STATUS: {} BODY: {}", statusCode, response.getBody());
        } catch (IOException e) {
            log.error("[SendGridService] Error while sending {} email", emailType.name(), e);
            throw new EmailSendingException(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_SENDING));
        }
    }

    private static Personalization createPersonalizationAndSetParameters(EmailType emailType, UUID codeId, String host) {
        var personalization = new Personalization();

        EMAIL_TEMPLATE_KEYS.forEach((key, value)->{
            var parameterSource = emailType.name().toLowerCase().concat(".").concat(value);
            personalization.addDynamicTemplateData(key, getMessageSource(parameterSource));
        });

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

    @Override
    public UUID sendMessagesToEmailForResetPassword(String email, EmailType emailType, String host) {
        var mail2send = new Mail();
        mail2send.setFrom(sender);
        mail2send.setTemplateId(emailType.template);

        UUID codeId = UUID.randomUUID();
        final var personalization = createPersonalizationForEmailForResetPassword(emailType, host);
        personalization.addTo(new Email(email));
        mail2send.addPersonalization(personalization);

        createRequest(mail2send, emailType);

        return codeId;
    }

    private static Personalization createPersonalizationForEmailForResetPassword(EmailType emailType, String host) {
        var personalization = new Personalization();

        EMAIL_TEMPLATE_KEYS.forEach((key, value) -> {
            var parameterSource = emailType.name().toLowerCase().concat(".password.").concat(value);
            personalization.addDynamicTemplateData(key, getMessageSource(parameterSource));
        });

        personalization.addDynamicTemplateData("url", host.concat(USER_SERVICE_PASSWORD_CONFIRM));

        return personalization;
    }

    private boolean isDurationMoreThanNumberOfDaysToKeepDeletedEmail(EmailConfirmationCode email) {
        Duration duration = Duration.between(email.getExpiryDate(), LocalDateTime.now());

        return duration.toDays() > numberOfDaysToKeepDeletedEmails;
    }
}
