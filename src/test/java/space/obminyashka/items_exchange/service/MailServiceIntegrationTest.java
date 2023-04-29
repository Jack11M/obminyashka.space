package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MailServiceIntegrationTest {

    @MockBean
    private EmailConfirmationCodeRepository emailRepository;
    @Autowired
    private MailService mailService;

    @Value("${number.of.days.to.keep.deleted.email.confirmation.token}")
    private int numberOfDaysToKeepDeletedEmails;

    @Test
    void testPermanentlyDeleteEmailConfirmationToken_shouldDeleteRequiredEmailConfirmationToken() {
        List<EmailConfirmationCode> emails = createTestEmails();
        assertEquals(3, emails.size());

        when(emailRepository.findAll()).thenReturn(emails);
        mailService.permanentlyDeleteEmailConfirmationCode();

        verify(emailRepository).delete(emails.get(0));

        for (int i = 1; i < emails.size(); i++) {
            verify(emailRepository, never()).delete(emails.get(i));
        }
    }

    private List<EmailConfirmationCode> createTestEmails() {
        EmailConfirmationCode shouldBeDeletedExpiredDateDayEighth = createEmailForDeleting(numberOfDaysToKeepDeletedEmails + 1);
        EmailConfirmationCode shouldNotBeDeletedExpiredDateNull = createEmailForDeleting(0);
        EmailConfirmationCode shouldNotBeDeletedExpiredDateDaySixth = createEmailForDeleting(numberOfDaysToKeepDeletedEmails - 1);

        return List.of(shouldBeDeletedExpiredDateDayEighth, shouldNotBeDeletedExpiredDateNull, shouldNotBeDeletedExpiredDateDaySixth);
    }

    private EmailConfirmationCode createEmailForDeleting(int delay) {
        EmailConfirmationCode emailConfirmationCode = new EmailConfirmationCode();
        emailConfirmationCode.setExpiryDate(LocalDateTime.now().minusDays(delay));

        return emailConfirmationCode;
    }
}


