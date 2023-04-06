package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.EMAIL_VALIDATE_CODE;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
public class EmailFlowTest extends BasicControllerTest {

    @Autowired
    public EmailFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @WithMockUser
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = { "email_confirmation_code/validate_code.yml"}, orderBy = {"id", "user_id", "expiry_date"},
            ignoreCols = {"password", "created", "updated", "last_online_time"})
    void validateEmail_whenDataCorrect_successfully() throws Exception {
        MvcResult mvcResult =  sendUriAndGetMvcResult(post(EMAIL_VALIDATE_CODE, UUID.fromString("ee36c78c-cfe9-11ed-b542-744ca1559076")), status().isOk());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.PositiveMessage.EMAIL_CONFIRMED)));
    }
}
