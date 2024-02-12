package space.obminyashka.items_exchange.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import space.obminyashka.items_exchange.service.impl.SendGridService;
import space.obminyashka.items_exchange.service.util.EmailType;
import space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy;

import java.io.IOException;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @InjectMocks
    private MessageSourceProxy messageSourceProxy;
    @Mock
    private SendGrid sendGrid;
    @Captor
    private ArgumentCaptor<Request> requestCapture;
    @InjectMocks
    private SendGridService mailService;

    @BeforeEach
    void setUp() {
        messageSourceProxy.setMSource(mock(MessageSource.class));
    }

    @ParameterizedTest
    @MethodSource("listEmailType")
    void sendMail_shouldPassTheFlow(EmailType emailType) throws IOException {
        when(sendGrid.api(any())).thenReturn(new Response());

        final var emailTo = "test@mail.ua";
        var expectedHost = "https://obminyashka.space";
        var confirmationCode = mailService.sendEmailTemplateAndGenerateConfrimationCode(emailTo, emailType, expectedHost);

        verify(sendGrid).api(requestCapture.capture());
        Request capturedRequest = requestCapture.getValue();
        verify(sendGrid).api(argThat(request -> capturedRequest.getMethod() == Method.POST &&
                capturedRequest.getEndpoint().equals("mail/send") &&
                capturedRequest.getBody().contains(emailTo)));
        assertNotNull(confirmationCode);
    }

    private static Stream<Arguments> listEmailType() {
        return Stream.of(
                Arguments.of(EmailType.REGISTRATION),
                Arguments.of(EmailType.RESET_PASSWORD)
        );
    }
}
