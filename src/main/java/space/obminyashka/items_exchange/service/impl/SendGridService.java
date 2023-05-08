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
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.*;

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

        final var personalization = populatePersonalization(subject, codeId, host);
        personalization.addTo(new Email(emailTo));
        mail2send.addPersonalization(personalization);

        var request = createMailRequest(mail2send);
        final var response = sendGrid.api(request);
        final var statusCode = response.getStatusCode();
        log.debug("[SendGridService] A sent email result. STATUS: {} BODY: {}", statusCode, response.getBody());
    }

    private static Personalization populatePersonalization(EmailType subject, UUID codeId, String host) {
        final var personalization = new Personalization();
        personalization.addDynamicTemplateData("subject", getMessageSource(subject.topic));
        personalization.addDynamicTemplateData("url", host.concat(EMAIL_VALIDATE_CODE.replace("{code}", codeId.toString())));

        switch (subject) {
            case REGISTRATION: {
                setPersonalizationParameters(personalization, RegistrationMessage.EMAIL_HEADER,
                        RegistrationMessage.EMAIL_GREETINGS, RegistrationMessage.EMAIL_INFORMATION,
                        RegistrationMessage.EMAIL_BENEFITS, RegistrationMessage.EMAIL_BUTTON,
                        RegistrationMessage.EMAIL_FOOTER);

                break;
            }
            case EMAIL_CHANGING: {
                setPersonalizationParameters(personalization, ChangingMessage.EMAIL_HEADER,
                        ChangingMessage.EMAIL_GREETINGS, ChangingMessage.EMAIL_INFORMATION,
                        ChangingMessage.EMAIL_BENEFITS, ChangingMessage.EMAIL_BUTTON,
                        ChangingMessage.EMAIL_FOOTER);

                break;
            }
        }

        return personalization;
    }

    private static void setPersonalizationParameters(Personalization personalization, String emailHeader,
                                                     String emailGreetings, String emailInformation,
                                                     String emailBenefits, String emailButton, String emailFooter) {
        personalization.addDynamicTemplateData("header", getMessageSource(emailHeader));
        personalization.addDynamicTemplateData("greetings", getMessageSource(emailGreetings));
        personalization.addDynamicTemplateData("information", getMessageSource(emailInformation));
        personalization.addDynamicTemplateData("benefits", getMessageSource(emailBenefits));
        personalization.addDynamicTemplateData("confirm", getMessageSource(emailButton));
        personalization.addDynamicTemplateData("footer", getMessageSource(emailFooter));
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
