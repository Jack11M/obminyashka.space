package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;


@SpringBootTest
@AutoConfigureMockMvc
class AdvertisementControllerIntegrationTest extends BasicControllerTest {
    @Autowired
    public AdvertisementControllerIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    void findPaginated_shouldBeThrownValidationException() throws Exception {
        int page = 0;
        int size = -12;
        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_SEARCH_PAGINATED_REQUEST_PARAMS, "KEYWORD", page, size), status().isBadRequest());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("must be greater than or equal to 0"));
    }

    @Test
    void findPaginatedByCategoryId_shouldReturnNotFound() throws Exception {
        long id = 0;
        int page = 0;
        int size = 12;
        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_SEARCH_PAGINATED_BY_CATEGORY_ID, id, page, size), status().isNotFound());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_CATEGORY_ID)));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateAdvertisement_shouldReturn400WhenNotValidAdvertisementFields() throws Exception {
        AdvertisementModificationDto existDtoForUpdate = AdvertisementDtoCreatingUtil
                .createExistAdvertisementDtoForUpdateWithNotValidFields();

        final var validationMessageSize = createInvalidSizeMessage(existDtoForUpdate.getSize(), "1", "50");
        final var validationMessageTopic = createInvalidSizeMessage(existDtoForUpdate.getTopic(), "3", "70");
        final var validationMessageDescription = createInvalidMaxSizeMessage(existDtoForUpdate.getDescription(), "255");
        final var validationMessageWishes = createInvalidMaxSizeMessage(existDtoForUpdate.getWishesToExchange(), "210");

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(ADV_ID, existDtoForUpdate.getId()), existDtoForUpdate, status().isBadRequest());
        final var responseText = mvcResult.getResponse().getContentAsString();

        Assertions.assertAll(
                () -> assertTrue(responseText.contains(validationMessageSize)),
                () -> assertTrue(responseText.contains(validationMessageDescription)),
                () -> assertTrue(responseText.contains(validationMessageTopic)),
                () -> assertTrue(responseText.contains(validationMessageWishes))
        );
    }

    public static String createInvalidSizeMessage(String dtoFieldValue, String minValidValue, String maxValidValue) {
        return MessageSourceUtil.getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_SIZE)
                .replace("${validatedValue}", dtoFieldValue)
                .replace("{min}", minValidValue)
                .replace("{max}", maxValidValue);
    }

    public static String createInvalidMaxSizeMessage(String dtoFieldValue, String maxValidValue) {
        return MessageSourceUtil.getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE)
                .replace("${validatedValue}", dtoFieldValue)
                .replace("{max}", maxValidValue);
    }
}
