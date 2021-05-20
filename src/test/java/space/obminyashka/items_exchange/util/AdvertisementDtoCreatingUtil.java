package space.obminyashka.items_exchange.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;

public class AdvertisementDtoCreatingUtil {

    private static final long existedLocationId = 1L;
    private static final String NOT_VALID_DESCRIPTION = createString(256);
    private static final String NOT_VALID_WISHES = createString(211);
    private static final String NOT_VALID_SIZE = createString(0);
    private static final String NOT_VALID_TOPIC = createString(2);

    public static AdvertisementModificationDto createNonExistAdvertisementModificationDto() {
        return getBuild(0L, "topic", "description", "hat",false, DealType.GIVEAWAY,
                existedLocationId, AgeRange.YOUNGER_THAN_1, Season.DEMI_SEASON, Gender.MALE, "M", 1L);
    }

    public static AdvertisementModificationDto createExistAdvertisementModificationDto() {
        return getBuild(1L, "topic", "description", "shoes", true, DealType.EXCHANGE,
                existedLocationId, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, "40", 2L);
    }

    public static AdvertisementModificationDto createExistAdvertisementModificationDtoForUpdate() {
        return getBuild(1L, "new topic", "new description", "BMW",true, DealType.EXCHANGE,
                existedLocationId, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.FEMALE, "50", 2L);
    }

    public static AdvertisementModificationDto createExistAdvertisementDtoForUpdateWithNotValidFields() {
        return getBuild(1L, NOT_VALID_TOPIC, NOT_VALID_DESCRIPTION, NOT_VALID_WISHES, true, DealType.EXCHANGE,
                existedLocationId, AgeRange.OLDER_THAN_14, Season.SUMMER, Gender.MALE, NOT_VALID_SIZE, 1L);
    }

    private static AdvertisementModificationDto getBuild(long advId, String topic, String description, String wishes, boolean offer,
                                             DealType exchange, long locationId, AgeRange age, Season season,
                                             Gender gender, String size, long subcatId) {
        return AdvertisementModificationDto.builder()
                .id(advId)
                .age(age)
                .size(size)
                .topic(topic)
                .gender(gender)
                .season(season)
                .dealType(exchange)
                .readyForOffers(offer)
                .locationId(locationId)
                .subcategoryId(subcatId)
                .wishesToExchange(wishes)
                .description(description)
                .build();
    }

    public static String createValidationMessage(String dtoFieldName, String dtoFieldValue, String minValidValue, String maxValidValue) {
        return MessageSourceUtil.getMessageSource("invalid.size")
                .replace("${validatedValue}",
                        "updateAdvertisement.dto." + dtoFieldName + ": " + dtoFieldValue)
                .replace("{min}", minValidValue)
                .replace("{max}", maxValidValue);
    }

    public static String createValidationMessage(String dtoFieldName, String dtoFieldValue, String maxValidValue) {
        return MessageSourceUtil.getMessageSource("invalid.max-size")
                .replace("${validatedValue}",
                        "updateAdvertisement.dto." + dtoFieldName + ": " + dtoFieldValue)
                .replace("{max}", maxValidValue);
    }

    public static String createValidationIdMessage(String dtoFieldName, long id, String errorMessage) {
        return MessageSourceUtil.getMessageSource(errorMessage)
                .replace("${validatedValue}",
                        "createAdvertisement.dto." + dtoFieldName + ": " + id);
    }

    public static boolean isResponseContainsExpectedResponse(String expectedResponse, MvcResult mvcResult) throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString().contains(expectedResponse);
    }

    public static void verifyAdvInternalEntityId(AdvertisementModificationDto dto,
                                                 String validationMessageProperty,
                                                 MockHttpServletRequestBuilder requestBuilder,
                                                 MockMvc mockMvc) throws Exception {

        var validationMessage = MessageSourceUtil.getMessageSource(validationMessageProperty);
        MvcResult mvcResult = mockMvc.perform(requestBuilder
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(isResponseContainsExpectedResponse(validationMessage, mvcResult));
    }

    private static String createString(int quantityOfCharsInNewsString){
        return "x".repeat(Math.max(0, quantityOfCharsInNewsString));
    }
}
