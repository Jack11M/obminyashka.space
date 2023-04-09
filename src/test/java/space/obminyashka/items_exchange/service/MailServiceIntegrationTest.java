package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import space.obminyashka.items_exchange.dao.EmailConfirmationTokenRepository;
import space.obminyashka.items_exchange.model.EmailConfirmationToken;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MailServiceIntegrationTest {

    @MockBean
    private EmailConfirmationTokenRepository emailRepository;
    @Autowired
    private MailService mailService;

    @Value("${number.of.days.to.keep.deleted.email.confirmation.token}")
    private int numberOfDaysToKeepDeletedEmails;

    @Test
    void testPermanentlyDeleteEmailConfirmationToken_shouldDeleteRequiredEmailConfirmationToken() {
        List<EmailConfirmationToken> emails = createTestEmails();
        assertEquals(3, emails.size());

        when(emailRepository.findAll()).thenReturn(emails);
        mailService.permanentlyDeleteEmailConfirmationToken();

        verify(emailRepository).delete(emails.get(0));

        for (int i = 1; i < emails.size(); i++) {
            verify(emailRepository, never()).delete(emails.get(i));
        }
    }

    private List<EmailConfirmationToken> createTestEmails() {
        EmailConfirmationToken shouldBeDeletedExpiredDateDayEighth = createEmailForDeleting(numberOfDaysToKeepDeletedEmails + 1);
        EmailConfirmationToken shouldNotBeDeletedExpiredDateNull = createEmailForDeleting(0);
        EmailConfirmationToken shouldNotBeDeletedExpiredDateDaySixth = createEmailForDeleting(numberOfDaysToKeepDeletedEmails - 1);

        return List.of(shouldBeDeletedExpiredDateDayEighth, shouldNotBeDeletedExpiredDateNull, shouldNotBeDeletedExpiredDateDaySixth);
    }

    private EmailConfirmationToken createEmailForDeleting(int delay) {
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
        emailConfirmationToken.setExpiryDate(LocalDateTime.now().minusDays(delay));

        return emailConfirmationToken;
    }
}


