package space.obminyashka.items_exchange.rest;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.util.data_producer.AdvertisementModificationDtoProducer;
import space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;


@SpringBootTest
@AutoConfigureMockMvc
class AdvertisementControllerIntegrationTest extends BasicControllerTest {
    private static final String VALID_ADV_ID = "65e3ee49-5927-40be-aafd-0461ce45f295";
    private static final String VALID_IMAGE_ID = "ebad2511-97c6-4221-a39f-a1b24a7d3251";
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
    @WithMockUser(username = "admin")
    void updateAdvertisement_shouldReturn400WhenNotValidAdvertisementFields() throws Exception {
        AdvertisementModificationDto existDtoForUpdate = AdvertisementModificationDtoProducer
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
        return MessageSourceProxy.getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_SIZE)
                .replace("${validatedValue}", dtoFieldValue)
                .replace("{min}", minValidValue)
                .replace("{max}", maxValidValue);
    }

    public static String createInvalidMaxSizeMessage(String dtoFieldValue, String maxValidValue) {
        return MessageSourceProxy.getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE)
                .replace("${validatedValue}", dtoFieldValue)
                .replace("{max}", maxValidValue);
    }

    @ParameterizedTest
    @ValueSource(strings = {"notExisted", ""})
    @WithMockUser("test")
    void testDefaultLocalizationAccordingToLanguageHeader(String languageHeader) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(ADV_DEFAULT_IMAGE, VALID_ADV_ID, VALID_IMAGE_ID)
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
        MvcResult mvcResult = mockMvc.perform(post(ADV_DEFAULT_IMAGE, VALID_ADV_ID, VALID_IMAGE_ID)
                        .header("Accept-Language", locale)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        final var errorMessage = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("Користувача не знайдено", new JSONObject(errorMessage).get("error"));
    }

    @ParameterizedTest
    @MethodSource("provideAdvertisementModificationDto")
    @WithMockUser(username = "admin")
    void createAdvertisement_shouldReturn400WhenBlankFieldsButWithoutBlankWish(AdvertisementModificationDto dto) throws Exception {
        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE,
                asJsonString(dto).getBytes());
        final var mvcResult = sendUriAndGetMvcResult(multipart(ADV).file(dtoJson), status().isBadRequest());
        var blankTopicMessage = getMessageSource(BLANK_TOPIC);
        var blankDescriptionMessage = getMessageSource(BLANK_DESCRIPTION);
        var blankWishesToExchangeMessage = getMessageSource(BLANK_WISHES_TO_EXCHANGE);

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessageContainingAll(blankTopicMessage, blankDescriptionMessage)
                .hasMessageNotContaining(blankWishesToExchangeMessage);
    }

    private static Stream<Arguments> provideAdvertisementModificationDto() {
        return Stream.of(
                Arguments.of(AdvertisementModificationDtoProducer.createNonExistAdvDtoWithBlankWishAndTrueReadyForOffer()),
                Arguments.of(AdvertisementModificationDtoProducer.createNonExistAdvDtoWithNotBlankWishAndTrueReadyForOffer()),
                Arguments.of(AdvertisementModificationDtoProducer.createNonExistAdvDtoWithNotBlankWishAndFalseReadyForOffer())
        );
    }
}
