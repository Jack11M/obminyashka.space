package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.repository.AdvertisementRepository;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;
import space.obminyashka.items_exchange.rest.exception.bad_request.BadRequestException;
import space.obminyashka.items_exchange.rest.exception.bad_request.IllegalIdentifierException;
import space.obminyashka.items_exchange.repository.enums.AgeRange;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.repository.enums.Season;
import space.obminyashka.items_exchange.repository.enums.Size;
import space.obminyashka.items_exchange.util.data_producer.AdvertisementModificationDtoProducer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage.ADVERTISEMENT_NOT_EXISTED_ID;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.util.JsonConverter.jsonToObject;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.*;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdvertisementFlowTest extends BasicControllerTest {

    private static final String VALID_ADV_ID = "65e3ee49-5927-40be-aafd-0461ce45f295";
    private static final String VALID_IMAGE_ID = "ebad2511-97c6-4221-a39f-a1b24a7d3251";
    private static final UUID INVALID_ID = UUID.randomUUID();
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

        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_SEARCH_PAGINATED_REQUEST_PARAMS, keyword, page, size), status().isOk());
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
        sendUriAndGetResultAction(get(ADV_FILTER)
                .queryParam("page", "2")
                .queryParam("size", "1"), status().isOk())
                .andExpect(jsonPath("$.content[0].advertisementId").value("4bd38c87-0f00-4375-bd8f-cd853f0eb9bd"))
                .andExpect(jsonPath("$.content[0].title").value("Dresses"));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnPageProperQuantityOfAdvertisementWithoutRequestAdvertisement() throws Exception {
        UUID excludeAdvertisementId = UUID.fromString("65e3ee49-5927-40be-aafd-0461ce45f295");

        sendUriAndGetResultAction(get(ADV_FILTER)
                .queryParam("advertisementFilter.excludeAdvertisementId", excludeAdvertisementId.toString())
                .queryParam("subcategoryFilterRequest.categoryId", "1")
                .queryParam("subcategoryFilterRequest.subcategoriesIdValues", "1"), status().isOk())

                .andExpect(jsonPath("$.content.length()")
                        .value(advertisementRepository.countByIdNotAndSubcategoryId(excludeAdvertisementId, List.of(1L))));
    }

    @Test
    @DisplayName("Should return all advertisements from DB as Page response")
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnPageThumbnailsResponse() throws Exception {
        sendUriAndGetResultAction(get(ADV_FILTER), status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(advertisementRepository.count()));
    }

    @Test
    @DisplayName("Should return all advertisements from DB (12 if there is more)")
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findPaginatedAsThumbnails_shouldReturnProperQuantityOfAdvertisementsThumbnails() throws Exception {
        sendUriAndGetResultAction(get(ADV_FILTER), status().isOk())
                .andExpect(jsonPath("$.content.length()").value(advertisementRepository.count()));
    }

    @Test
    @DisplayName("Should return total size of existed advertisements")
    @DataSet("database_init.yml")
    void countAdvertisements_shouldReturnTotalAmount() throws Exception {
        final var count = advertisementRepository.count();
        final var mvcResult = sendUriAndGetMvcResult(get(ADV_TOTAL), status().isOk());
        assertEquals(String.valueOf(count), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementIfExists() throws Exception {
        sendUriAndGetResultAction(get(ADV_ID, VALID_ADV_ID), status().isOk())
                .andExpect(jsonPath("$.advertisementId").value(VALID_ADV_ID))
                .andExpect(jsonPath("$.topic").value("topic"))
                .andExpect(jsonPath("$.ownerName").value("super admin"))
                .andExpect(jsonPath("$.age").value("14+"))
                .andExpect(jsonPath("$.createdDate").value("01.01.2019"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findAdvertisementBySearchParameters_shouldReturnAdvertisementsByNecessaryParameters() throws Exception {
        sendUriAndGetResultAction(get(ADV_FILTER)
                .queryParam("subcategoryFilterRequest.categoryId", "1")
                .queryParam("subcategoryFilterRequest.subcategoriesIdValues", "1")
                .queryParam("advertisementFilter.clothingSizes", Size.Clothing.FIFTY_SEVEN_2_SIXTY_TWO.getRange()), status().isOk())
                .andExpect(jsonPath("$.content.length()").value("3"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findAdvertisementBySearchParameters_shouldReturnAdvertisementsByAllParameters() throws Exception {
        sendUriAndGetResultAction(get(ADV_FILTER)
                .queryParam("subcategoryFilterRequest.categoryId", "1")
                .queryParam("subcategoryFilterRequest.subcategoriesIdValues", "1")
                .queryParam("advertisementFilter.locationId", "2c5467f3-b7ee-48b1-9451-7028255b757b")
                .queryParam("advertisementFilter.gender", Gender.FEMALE.name())
                .queryParam("advertisementFilter.age", AgeRange.FROM_10_TO_12.getValue(), AgeRange.FROM_6_TO_9.getValue())
                .queryParam("advertisementFilter.clothingSizes", Size.Clothing.FIFTY_SEVEN_2_SIXTY_TWO.getRange())
                .queryParam("advertisementFilter.season", Season.SUMMER.name(), Season.WINTER.name()), status().isOk())
                .andExpect(jsonPath("$.content.length()").value("2"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findAdvertisementBySearchParameters_shouldBeThrownValidationException_WhenSizeFromIncorrectSubcategoryClothes() throws Exception {
        String subcategoryId = "1";
        String categoryId = "1";
        String sizeShoes = String.valueOf(Size.Shoes.ELEVEN_POINT_FIVE.getLength());
        String sizeClothing = Size.Clothing.FIFTY_SEVEN_2_SIXTY_TWO.getRange();

        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_FILTER)
                .queryParam("subcategoryFilterRequest.categoryId", categoryId)
                .queryParam("subcategoryFilterRequest.subcategoriesIdValues", subcategoryId)
                .queryParam("advertisementFilter.shoesSizes", sizeShoes)
                .queryParam("advertisementFilter.clothingSizes", sizeClothing), status().isBadRequest());
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessageContaining(getMessageSource(INVALID_SIZE_COMBINATION));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void findAdvertisementBySearchParameters_shouldBeThrownBadRequestException_WhenSubcategoryNotBelongCategory() throws Exception {
        String categoryId = "2";
        String sizeShoes = String.valueOf(Size.Shoes.ELEVEN_POINT_FIVE.getLength());
        MvcResult mvcResult = sendUriAndGetMvcResult(get(ADV_FILTER)
                .queryParam("subcategoryFilterRequest.categoryId", categoryId)
                .queryParam("subcategoryFilterRequest.subcategoriesIdValues", "14", "1", "3")
                .queryParam("advertisementFilter.shoesSizes", sizeShoes), status().isBadRequest());
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(getParametrizedMessageSource(INVALID_CATEGORY_SUBCATEGORY_COMBINATION,
                        "[14]", categoryId));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/create.yml", orderBy = {"created", "name"},
            ignoreCols = {"id", "default_photo", "created", "updated", "advertisement_id", "resource"})
    void createAdvertisement_shouldCreateValidAdvertisement() throws Exception {
        var nonExistDto = AdvertisementModificationDtoProducer.createNonExistAdvertisementModificationDto();
        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE, asJsonString(nonExistDto).getBytes());
        final var contentJson = sendUriAndGetResultAction(multipart(ADV).file(jpeg).file(dtoJson), status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        assertEquals(nonExistDto, jsonToObject(contentJson, AdvertisementModificationDto.class));
        assertEquals(6, advertisementRepository.count());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "database_init.yml", orderBy = {"created", "name", "resource", "birth_date"})
    void createAdvertisement_shouldReturn400WhenBlankFields() throws Exception {
        var nonExistDto = AdvertisementModificationDtoProducer.createNonExistAdvertisementModificationDtoWithBlankFields();
        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE,
                asJsonString(nonExistDto).getBytes());
        final var mvcResult = sendUriAndGetMvcResult(multipart(ADV).file(jpeg).file(dtoJson), status().isBadRequest());
        var blankTopicMessage = getMessageSource(BLANK_TOPIC);
        var blankDescriptionMessage = getMessageSource(BLANK_DESCRIPTION);
        var blankWishesToExchangeMessage = getMessageSource(BLANK_WISHES_TO_EXCHANGE);

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessageContainingAll(blankTopicMessage, blankDescriptionMessage, blankWishesToExchangeMessage);
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/update.yml", orderBy = {"created", "name", "resource"}, ignoreCols = "updated")
    void updateAdvertisement_shouldUpdateExistedAdvertisement() throws Exception {
        AdvertisementModificationDto existDtoForUpdate =
                AdvertisementModificationDtoProducer.createExistAdvertisementModificationDtoForUpdate();
        sendDtoAndGetResultAction(put(ADV_ID, existDtoForUpdate.getId()), existDtoForUpdate, status().isAccepted())
                .andExpect(jsonPath("$.description").value("new description"))
                .andExpect(jsonPath("$.topic").value("new topic"))
                .andExpect(jsonPath("$.wishesToExchange").value("BMW"));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void createAdvertisement_shouldReturn400WhenInvalidLocationAndSubcategoryId() throws Exception {
        final var dto = AdvertisementModificationDtoProducer.createNonExistAdvertisementModificationDto();

        dto.setLocationId(INVALID_ID);
        dto.setSubcategoryId(99L);
        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE, asJsonString(dto).getBytes());
        final var mvcResult = sendUriAndGetMvcResult(multipart(ADV).file(jpeg).file(dtoJson), status().isBadRequest());

        var validationLocationIdMessage = getMessageSource(INVALID_LOCATION_ID);
        var validationSubcategoryIdMessage = getMessageSource(INVALID_SUBCATEGORY_ID);

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(IllegalIdentifierException.class)
                .hasMessageContainingAll(validationLocationIdMessage, validationSubcategoryIdMessage);
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/delete.yml", orderBy = {"created", "name"})
    void deleteAdvertisement_shouldDeleteExistedAdvertisement() throws Exception {
        sendUriAndGetMvcResult(delete(ADV_ID, VALID_ADV_ID), status().isOk());
    }

    @ParameterizedTest
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    @MethodSource("provideDeleteAdvertisementTestData")
    void deleteAdvertisement_whenIncorrectId_shouldThrowException(String uuid,
                                                                  String message,
                                                                  Exception exception,
                                                                  ResultMatcher resultMatcher) throws Exception {
        var validationLocationIdMessage = getParametrizedMessageSource(message, uuid);
        var mvcResult = sendUriAndGetMvcResult(delete(ADV_ID, uuid), resultMatcher);

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(exception.getClass())
                .hasMessageContaining(validationLocationIdMessage);
    }

    private static Stream<Arguments> provideDeleteAdvertisementTestData() {
        return Stream.of(
                Arguments.of(String.valueOf(INVALID_ID),
                        ADVERTISEMENT_NOT_EXISTED_ID,
                        new EntityNotFoundException(ADVERTISEMENT_NOT_EXISTED_ID),
                        status().isNotFound()),

                Arguments.of(VALID_ADV_ID,
                        USER_NOT_OWNER,
                        new IllegalOperationException(USER_NOT_OWNER),
                        status().isForbidden())
        );
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/setDefaultImage.yml", orderBy = {"created", "name", "resource"},
            ignoreCols = {"created", "updated"})
    void setDefaultImage_success() throws Exception {
        sendUriAndGetMvcResult(post(ADV_DEFAULT_IMAGE, VALID_ADV_ID, VALID_IMAGE_ID), status().isOk());
    }

    @ParameterizedTest
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    @MethodSource("provideTestIds")
    void setDefaultImage_shouldReturn400WhenNotValidAdvertisementId(UUID firstId, UUID secondId) throws Exception {
        sendUriAndGetMvcResult(post(ADV_DEFAULT_IMAGE, firstId, secondId), status().isBadRequest());
    }

    private static Stream<Arguments> provideTestIds() {
        return Stream.of(
                Arguments.of(VALID_ADV_ID, INVALID_ID),
                Arguments.of(INVALID_ID, VALID_ADV_ID)
        );
    }
}
