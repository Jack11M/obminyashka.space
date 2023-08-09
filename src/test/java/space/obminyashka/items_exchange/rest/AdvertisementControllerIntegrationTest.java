package space.obminyashka.items_exchange.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;
import space.obminyashka.items_exchange.util.data_producer.AdvertisementModificationDtoProducer;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;


@SpringBootTest
@AutoConfigureMockMvc
class AdvertisementControllerIntegrationTest extends BasicControllerTest {
    private final MessageSource messageSource;

    @Autowired
    public AdvertisementControllerIntegrationTest(MockMvc mockMvc, MessageSource messageSource) {
        super(mockMvc);
        this.messageSource = messageSource;
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

    public String createInvalidSizeMessage(String dtoFieldValue, String minValidValue, String maxValidValue) {
        return messageSource.getMessage(INVALID_SIZE, null, Locale.ENGLISH)
                .replace("${validatedValue}", dtoFieldValue)
                .replace("{min}", minValidValue)
                .replace("{max}", maxValidValue);
    }

    public String createInvalidMaxSizeMessage(String dtoFieldValue, String maxValidValue) {
        return messageSource.getMessage(INVALID_MAX_SIZE, null, Locale.ENGLISH)
                .replace("${validatedValue}", dtoFieldValue)
                .replace("{max}", maxValidValue);
    }

    @ParameterizedTest
    @MethodSource("provideLocalizationTestData")
    @WithMockUser("test")
    void testForbiddenAccessErrorMessageResponseAccordingToLanguageHeader(String languageHeader, Locale expectedMessageLocale) throws Exception {
        final var advertisementId = UUID.randomUUID();
        final var httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT_LANGUAGE, languageHeader);

        MvcResult mvcResult = sendUriWithHeadersAndGetMvcResult(delete(ADV_ID, advertisementId), status().isForbidden(), httpHeaders);

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(IllegalOperationException.class)
                .hasMessage(messageSource.getMessage(USER_NOT_OWNER, new UUID[]{advertisementId}, expectedMessageLocale));
    }

    private static Stream<Arguments> provideLocalizationTestData() {
        return Stream.of(
                Arguments.of("notExisted", Locale.ENGLISH),
                Arguments.of("", Locale.ENGLISH),
                Arguments.of("ua", Locale.of("UA")),
                Arguments.of("ua-UA", Locale.of("UA"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAdvertisementModificationDto")
    @WithMockUser(username = "admin")
    void createAdvertisement_shouldReturn400WhenBlankFieldsButWithoutBlankWish(AdvertisementModificationDto dto) throws Exception {
        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE,
                asJsonString(dto).getBytes());
        final var mvcResult = sendUriAndGetMvcResult(multipart(ADV).file(dtoJson), status().isBadRequest());
        var blankTopicMessage = messageSource.getMessage(BLANK_TOPIC, null, Locale.ENGLISH);
        var blankDescriptionMessage = messageSource.getMessage(BLANK_DESCRIPTION, null, Locale.ENGLISH);
        var blankWishesToExchangeMessage = messageSource.getMessage(BLANK_WISHES_TO_EXCHANGE, null, Locale.ENGLISH);

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
