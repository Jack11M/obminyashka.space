package space.obminyashka.items_exchange.end2end;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dto.AdvertisementFilterDto;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.dto.AdvertisementTitleDto;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil;
import space.obminyashka.items_exchange.util.JsonConverter;
import space.obminyashka.items_exchange.util.MessageSourceUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil.isResponseContainsExpectedResponse;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class AdvertisementFlowTest extends BasicControllerTest {

    private static final long VALID_ID = 1L;
    private static final long INVALID_ID = 999L;
    private final AdvertisementRepository advertisementRepository;
    private final MockMultipartFile jpeg;

    @Autowired
    public AdvertisementFlowTest(MockMvc mockMvc, AdvertisementRepository advertisementRepository) throws IOException {
        super(mockMvc);
        this.advertisementRepository = advertisementRepository;
        jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Path.of("src/test/resources/image/test-image.jpeg")));
    }

    @ParameterizedTest
    @MethodSource("getSearchKeywords")
    @DataSet("database_init.yml")
    void findPaginated_shouldReturnSearchResults(String keyword, int expectedResultQuantity) throws Exception {
        int page = 0;
        int size = 12;

        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_SEARCH_PAGINATED, keyword, page, size), status().isOk());
        final var totalElements = Stream.of(mvcResult.getResponse().getContentAsString().split(","))
                .filter(s -> s.startsWith("\"totalElements"))
                .map(s -> s.substring(s.length() - 1))
                .map(Integer::parseInt)
                .findFirst()
                .orElse(0);

        assertEquals(expectedResultQuantity, totalElements);
    }

    private static Stream<Arguments> getSearchKeywords() {
        return Stream.of(
                Arguments.of("blouses description", 1), // full description matching
                Arguments.of("pajamas", 1), // full topic matching
                Arguments.of("description", 5), // partial description matching
                Arguments.of("ses", 2) // partial topic matching
        );
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnSpecificAdvertisementTitleDto() throws Exception {
        int page = 2;
        int size = 1;
        sendUriAndGetResultAction(get(ADV_THUMBNAIL_PARAMS, page, size), status().isOk())
                .andExpect(jsonPath("$[0].advertisementId").value("3"))
                .andExpect(jsonPath("$[0].title").value("Dresses"))
                .andExpect(jsonPath("$[0].ownerName").value("admin"));
    }

    @Test
    @DisplayName("Should return all advertisements from DB (12 if there is more)")
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnProperQuantityOfAdvertisementsThumbnails() throws Exception {
        sendUriAndGetResultAction(get(ADV_THUMBNAIL), status().isOk())
                .andExpect(jsonPath("$.length()").value(advertisementRepository.findAll().size()));
    }

    @Test
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementIfExists() throws Exception {
        sendUriAndGetResultAction(get(ADV_ID, VALID_ID), status().isOk())
                .andExpect(jsonPath("$.advertisementId").value(VALID_ID))
                .andExpect(jsonPath("$.topic").value("topic"))
                .andExpect(jsonPath("$.ownerName").value("super admin"))
                .andExpect(jsonPath("$.age").value("14+"))
                .andExpect(jsonPath("$.createdDate").value("01.01.2019"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementsIfAnyValueExists() throws Exception {
        AdvertisementFilterDto dto = AdvertisementFilterDto.builder()
                .season(Season.SUMMER)
                .gender(Gender.FEMALE)
                .age(AgeRange.FROM_10_TO_12)
                .build();

        MvcResult mvcResult = sendDtoAndGetMvcResult(post(ADV_FILTER), dto, status().isOk());

        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdvertisementTitleDto[] advertisementDtos = JsonConverter.jsonToObject(contentAsString,
                AdvertisementTitleDto[].class);

        Assertions.assertAll(
                () -> assertEquals(1, advertisementDtos.length),
                () -> assertEquals(4, advertisementDtos[0].getAdvertisementId()),
                () -> assertEquals("admin" , advertisementDtos[0].getOwnerName())
        );
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/create.yml", ignoreCols = {"default_photo", "created", "updated", "resource"})
    void createAdvertisement_shouldCreateValidAdvertisement() throws Exception {
        AdvertisementModificationDto nonExistDto =
                AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto();
        sendDtoAndGetResultAction(multipart(ADV).file(jpeg), nonExistDto, status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/update.yml", ignoreCols = "updated")
    void updateAdvertisement_shouldUpdateExistedAdvertisement() throws Exception {
        AdvertisementModificationDto existDtoForUpdate =
                AdvertisementDtoCreatingUtil.createExistAdvertisementModificationDtoForUpdate();
        sendDtoAndGetResultAction(put(ADV), existDtoForUpdate, status().isAccepted())
                .andExpect(jsonPath("$.description").value("new description"))
                .andExpect(jsonPath("$.topic").value("new topic"))
                .andExpect(jsonPath("$.wishesToExchange").value("BMW"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void createAdvertisement_shouldReturn400WhenInvalidLocationAndSubcategoryId() throws Exception {
        final var dto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto();

        dto.setLocationId(INVALID_ID);
        dto.setSubcategoryId(INVALID_ID);
        final var mvcResult = sendDtoAndGetMvcResult(multipart(ADV).file(jpeg), dto, status().isBadRequest());

        var validationLocationIdMessage = MessageSourceUtil.getMessageSource("invalid.location.id");
        var validationSubcategoryIdMessage = MessageSourceUtil.getMessageSource("invalid.subcategory.id");
        Assertions.assertAll(
                () -> assertTrue(isResponseContainsExpectedResponse(validationLocationIdMessage, mvcResult)),
                () -> assertTrue(isResponseContainsExpectedResponse(validationSubcategoryIdMessage, mvcResult))
        );
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/delete.yml")
    void deleteAdvertisement_shouldDeleteExistedAdvertisement() throws Exception {
        sendUriAndGetMvcResult(delete(ADV_ID, VALID_ID), status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/setDefaultImage.yml", ignoreCols = {"created", "updated"})
    void setDefaultImage_success() throws Exception {
        sendUriAndGetMvcResult(post(ADV_DEFAULT_IMAGE, VALID_ID, VALID_ID), status().isOk());
    }


    @ParameterizedTest
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @MethodSource("provideTestIds")
    void setDefaultImage_shouldReturn400WhenNotValidAdvertisementId(long firstId, long secondId) throws Exception {
        sendUriAndGetMvcResult(post(ADV_DEFAULT_IMAGE, firstId, secondId), status().isBadRequest());
    }

    private static Stream<Arguments> provideTestIds() {
        return Stream.of(
                Arguments.of(VALID_ID, INVALID_ID),
                Arguments.of(INVALID_ID, VALID_ID),
                Arguments.of(-1L, -2L)
        );
    }
}
