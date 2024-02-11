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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.request.AdvertisementFilterRequest;
import space.obminyashka.items_exchange.util.data_producer.AdvertisementModificationDtoProducer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
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
        final var advertisementFilterRequest = new AdvertisementFilterRequest()
                .setPage(0)
                .setSize(-12);
        MvcResult mvcResult = sendDtoAndGetMvcResult(post(ADV_FILTER), advertisementFilterRequest, status().isBadRequest());
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

    @Test
    @WithMockUser(username = "admin")
    void createAdvertisement_shouldReturn400WhenInvalidSizeForCategory() throws Exception {
        final var dto = AdvertisementModificationDtoProducer.createNonExistAdvertisementModificationDto();
        dto.setSize("invalidSize");

        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE,
                asJsonString(dto).getBytes());
        final var jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Path.of("src/test/resources/image/test-image.jpeg")));

        final var mvcResult = sendUriAndGetMvcResult(multipart(ADV).file(jpeg).file(dtoJson), status().isBadRequest());

        var validationSizeMessage = getParametrizedMessageSource(INVALID_ENUM_VALUE, dto.getSize());

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(validationSizeMessage);
    }

    private static Stream<Arguments> provideAdvertisementModificationDto() {
        return Stream.of(
                Arguments.of(AdvertisementModificationDtoProducer.createNonExistAdvDtoWithBlankWishAndTrueReadyForOffer()),
                Arguments.of(AdvertisementModificationDtoProducer.createNonExistAdvDtoWithNotBlankWishAndTrueReadyForOffer()),
                Arguments.of(AdvertisementModificationDtoProducer.createNonExistAdvDtoWithNotBlankWishAndFalseReadyForOffer())
        );
    }
}
