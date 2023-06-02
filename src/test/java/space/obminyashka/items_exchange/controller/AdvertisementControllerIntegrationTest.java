package space.obminyashka.items_exchange.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.exception.CategoryIdNotFoundException;
import space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil;
import space.obminyashka.items_exchange.util.MessageSourceUtil;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.INVALID_CATEGORY_ID;


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
    void findAdvertisementsHavingCategory_shouldReturnNotFound_whenCategoryIdDoesNotExist() throws Exception {
        long id = 999;
        int page = 0;
        int size = 12;
        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_SEARCH_PAGINATED_BY_CATEGORY_ID, id, page, size), status().isNotFound());
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(CategoryIdNotFoundException.class)
                .hasMessage(getParametrizedMessageSource(INVALID_CATEGORY_ID, id));
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

    @ParameterizedTest
    @ValueSource(strings = {"notExisted", ""})
    @WithMockUser("test")
    void testDefaultLocalizationAccordingToLanguageHeader(String languageHeader) throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(ADV_ID, UUID.randomUUID())
                        .header("Accept-Language", languageHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        final var errorMessage = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("User not found", new JSONObject(errorMessage).get("error"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ua", "ua-UA"})
    @WithMockUser("test")
    void testUkrainianLocalizationWithAccept_LanguageHeaderIsUA(String locale) throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(ADV_ID, UUID.randomUUID())
                        .header("Accept-Language", locale)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        final var errorMessage = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("Користувача не знайдено", new JSONObject(errorMessage).get("error"));
    }
}
