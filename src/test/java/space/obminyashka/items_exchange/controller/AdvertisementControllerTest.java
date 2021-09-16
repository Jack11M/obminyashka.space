package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil.createValidationMessage;
import static space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil.isResponseContainsExpectedResponse;


@SpringBootTest
@AutoConfigureMockMvc
class AdvertisementControllerTest extends BasicControllerTest {

    private static final long INVALID_ID = 999L;

    @Autowired
    public AdvertisementControllerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    void findPaginated_shouldBeThrownValidationException() throws Exception {
        int page = 0;
        int size = -12;
        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_SEARCH_PAGINATED, "KEYWORD", page, size), status().isBadRequest());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("must be greater than or equal to 0"));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateAdvertisement_shouldReturn400WhenNotValidAdvertisementFields() throws Exception {
        AdvertisementModificationDto existDtoForUpdate = AdvertisementDtoCreatingUtil
                .createExistAdvertisementDtoForUpdateWithNotValidFields();

        final var validationMessageSize =
                createValidationMessage("size", existDtoForUpdate.getSize(), "1", "50");
        final var validationMessageTopic =
                createValidationMessage("topic", existDtoForUpdate.getTopic(), "3", "70");
        final var validationMessageDescription =
                createValidationMessage("description", existDtoForUpdate.getDescription(), "255");
        final var validationMessageWhishes =
                createValidationMessage("wishesToExchange", existDtoForUpdate.getWishesToExchange(), "210");

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(ADV), existDtoForUpdate, status().isBadRequest());

        Assertions.assertAll(
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageSize, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageDescription, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageTopic, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageWhishes, mvcResult))
        );
    }

    @ParameterizedTest
    @WithMockUser(username = "admin")
    @MethodSource(value = "createTestDto")
    void createAdvertisement_shouldReturn400WhenAdvIdIsNotZero(AdvertisementModificationDto dto) throws Exception {
        dto.setId(INVALID_ID);
        final var mvcResult = sendDtoAndGetMvcResult(post(ADV), dto, status().isBadRequest());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"error\":\"Validation error(s)"));
    }

    private static Stream<Arguments> createTestDto() {
        return Stream.of(
                Arguments.of(AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto()),
                Arguments.of(AdvertisementDtoCreatingUtil.createExistAdvertisementModificationDto()
                ));
    }
}
