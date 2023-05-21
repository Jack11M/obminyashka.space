package space.obminyashka.items_exchange.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.USER_CHILD;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.generateTestChildren;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getTestChildren;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ExceptionMessage.CHILDREN_AMOUNT;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.INVALID_CHILD_AGE;

@SpringBootTest
@AutoConfigureMockMvc
class ChildControllerIntegrationTest extends BasicControllerTest {
    @Autowired
    private MessageSource messageSource;

    private static final int MAX_CHILDREN_AMOUNT = 10;

    @Autowired
    public ChildControllerIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_badTotalAmount_shouldReturnBadRequest() throws Exception {
        var badTotalAmountChildDto = generateTestChildren(MAX_CHILDREN_AMOUNT + 1);

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), badTotalAmountChildDto, status().isBadRequest());

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage(CHILDREN_AMOUNT, null, Locale.ENGLISH)
                        .replace("{max}", String.valueOf(MAX_CHILDREN_AMOUNT)));
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_invalidChildAge_shouldReturnHttpStatusBadRequest() throws Exception {
        var childDto = getTestChildren(2001);

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), childDto, status().isBadRequest());

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(messageSource.getMessage(INVALID_CHILD_AGE, null, Locale.ENGLISH));
    }
}
