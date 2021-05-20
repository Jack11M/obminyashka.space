package space.obminyashka.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil.*;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;


@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class AdvertisementControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    private long validId;
    private long notValidId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        notValidId = 999L;
    }

    @Test
    void findPaginated_shouldBeThrownValidationException() throws Exception {
        int page = 0;
        int size = -12;
        MvcResult mvcResult = mockMvc.perform(get("/adv?page={page}&size={size}", page, size))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("must be greater than or equal to 0"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void setDefaultImage_shouldReturn400WhenNotValidAdvertisementId() throws Exception {
        mockMvc.perform(
                post("/adv/default-image/{advertisementId}/{imageId}", notValidId, validId))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void setDefaultImage_shouldReturn400WhenNotValidImageId() throws Exception {
        mockMvc.perform(
                post("/adv/default-image/{advertisementId}/{imageId}", validId, notValidId))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void setDefaultImage_shouldReturn400WhenNegativeParameterReceived() throws Exception {
        mockMvc.perform(
                post("/adv/default-image/{advertisementId}/{imageId}", -1L, -2L))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnSpecificAdvertisementTitleDto() throws Exception {
        int page = 2;
        int size = 1;
        mockMvc.perform(get("/adv/thumbnail?page={page}&size={size}", page, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].advertisementId").value("3"))
                .andExpect(jsonPath("$[0].title").value("Dresses"))
                .andExpect(jsonPath("$[0].ownerName").value("admin"));
    }

    @Test
    @DisplayName("Should return all advertisements from DB (12 if there is more)")
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnProperQuantityOfAdvertisementsThumbnails() throws Exception {
       mockMvc.perform(get("/adv/thumbnail"))
                .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(advertisementRepository.findAll().size()));
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

        MvcResult mvcResult = mockMvc.perform(put("/adv")
                .content(asJsonString(existDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assertions.assertAll(
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageSize, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageDescription, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageTopic, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationMessageWhishes, mvcResult))
        );
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void createAdvertisement_shouldReturn400WhenLocationIdNotExist() throws Exception {
        AdvertisementModificationDto nonExistedDto =
                AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto();
        nonExistedDto.setLocationId(notValidId);
        verifyAdvInternalEntityId(nonExistedDto, "invalid.location.id", post("/adv"), mockMvc);
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void updateAdvertisement_shouldReturn400WhenSubcategoryIdNotExist() throws Exception {
        AdvertisementModificationDto existedDto =
                AdvertisementDtoCreatingUtil.createExistAdvertisementModificationDto();
        existedDto.setSubcategoryId(notValidId);
        verifyAdvInternalEntityId(existedDto, "invalid.subcategory.id", put("/adv"), mockMvc);
    }

    @Test
    @WithMockUser(username = "admin")
    void createAdvertisement_shouldReturn400WhenAdvIdIsNotZero() throws Exception {
        AdvertisementModificationDto nonExistedDto =
                AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto();
        nonExistedDto.setId(notValidId);
        mockMvc.perform(post("/adv"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void updateAdvertisement_shouldReturn404WhenAdvIdNotExist() throws Exception {
        AdvertisementModificationDto existedDto =
                AdvertisementDtoCreatingUtil.createExistAdvertisementModificationDto();
        existedDto.setId(notValidId);
        mockMvc.perform(put("/put"))
                .andExpect(status().isNotFound());
    }
}
