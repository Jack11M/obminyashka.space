package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.request.ValidationEmailRequest;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.EMAIL_RESEND_CODE;
import static space.obminyashka.items_exchange.rest.api.ApiKey.EMAIL_VALIDATE_CODE;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class EmailFlowTest extends BasicControllerTest {

    @Autowired
    public EmailFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    private static final String DOMAIN_URL = "https://obminyashka.space";


    @Test
    @WithMockUser
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = {"email_confirmation_code/validate_code.yml"})
    void validateEmail_whenDataCorrect_successfully() throws Exception {
        final var exceptedCode = UUID.fromString("ee36c78c-cfe9-11ed-b542-744ca1559076");
        MvcResult mvcResult = sendUriAndGetMvcResult(get(EMAIL_VALIDATE_CODE, exceptedCode), status().isOk());

        final var exceptedResponse = getMessageSource(ResponseMessagesHandler.PositiveMessage.EMAIL_CONFIRMED);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(exceptedResponse);
    }

    @Test
    @WithMockUser
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = {"email_confirmation_code/resend_validate_code.yml"})
    void resendValidateCode_whenDataCorrect_successfully() throws Exception {
        final var validationEmailRequest = new ValidationEmailRequest("admin@gmail.com");
        MvcResult mvcResult = sendDtoAndGetMvcResult(post(EMAIL_RESEND_CODE)
                .header(HttpHeaders.HOST, DOMAIN_URL), validationEmailRequest, status().isOk());

        final var exceptedResponse = getMessageSource(ResponseMessagesHandler.PositiveMessage.EMAIL_RESEND_CODE);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(exceptedResponse);
    }

}
