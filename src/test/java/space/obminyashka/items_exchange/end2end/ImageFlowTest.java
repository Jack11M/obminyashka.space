package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.dbunit.util.Base64;
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
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.USER_NOT_OWNER;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DataSet("database_init.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ImageFlowTest extends BasicControllerTest {
    private static final String EXISTED_ADV_ID = "65e3ee49-5927-40be-aafd-0461ce45f295";
    private static final String TEST_JPEG = "test image jpeg";
    private static final String TEST_PNG = "test image png";
    private final MockMultipartFile txt = new MockMultipartFile("image", "text.txt", MediaType.TEXT_PLAIN_VALUE, "plain text".getBytes());

    @Autowired
    public ImageFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    void getByAdvertisementId_shouldReturnAllImages() throws Exception {
        sendUriAndGetResultAction(get(IMAGE_RESOURCE, EXISTED_ADV_ID), status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1]").value(Base64.encodeString(TEST_PNG)));
    }

    @Test
    void getImageLinksByAdvertisementId_shouldReturnAllImageLinks() throws Exception {
        sendUriAndGetResultAction(get(IMAGE_BY_ADV_ID, EXISTED_ADV_ID), status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("ebad2511-97c6-4221-a39f-a1b24a7d3251"))
                .andExpect(jsonPath("$[0].resource").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1].id").value("e6a85b1b-6c6f-4bbb-b336-f68e43bb69f9"))
                .andExpect(jsonPath("$[1].resource").value(Base64.encodeString(TEST_PNG)));
    }

    @ParameterizedTest
    @MethodSource("getAdvertisementIdAndExpectedResult")
    void countImagesInAdvertisement(String advertisementId, ResultMatcher expectedStatus, String expectedResponse) throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(get(IMAGE_IN_ADV_COUNT, advertisementId), expectedStatus);
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
    }

    private static Stream<Arguments> getAdvertisementIdAndExpectedResult() {
        return Stream.of(
                Arguments.of(EXISTED_ADV_ID, status().isOk(), "2"),
                Arguments.of("7de4e6bb-fc91-439b-9a3e-0cc5e707b4c7", status().isOk(), "0"),
                Arguments.of(UUID.randomUUID().toString(), status().isNotFound(), "")
        );
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn415WhenNotSupportedType() throws Exception {
        sendUriAndGetMvcResult(multipart(IMAGE_BY_ADV_ID, EXISTED_ADV_ID).file(txt), status().isUnsupportedMediaType());
    }

    @WithMockUser("admin")
    @Test
    @Commit
    @ExpectedDataSet(value = "image/delete.yml", orderBy = {"created", "name"}, ignoreCols = {"created", "updated"})
    void deleteImages_shouldDeleteMultipleImageWhenUserOwnsThemAll() throws Exception {
        sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, EXISTED_ADV_ID)
                        .param("ids", "ebad2511-97c6-4221-a39f-a1b24a7d3251", "e6a85b1b-6c6f-4bbb-b336-f68e43bb69f9"),
                status().isOk());
    }

    @WithMockUser
    @Test
    void deleteImages_shouldThrow403WhenUserNotOwnAdvertisement() throws Exception {
        final MvcResult mvcResult = sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, EXISTED_ADV_ID)
                        .param("ids", UUID.randomUUID().toString()),
                status().isForbidden());

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(IllegalOperationException.class)
                .hasMessage(getParametrizedMessageSource(USER_NOT_OWNER, EXISTED_ADV_ID));
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldSaveImage() throws Exception {
        var jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Path.of("src/test/resources/image/test-image.jpeg")));
        mockMvc.perform(multipart(IMAGE_BY_ADV_ID, EXISTED_ADV_ID)
                .file(jpeg)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }
}
