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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import space.obminyashka.items_exchange.BasicControllerTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DataSet("database_init.yml")
class ImageFlowTest extends BasicControllerTest {

    private static final String TEST_JPEG = "test image jpeg";
    private static final String TEST_PNG = "test image png";
    private final MockMultipartFile txt = new MockMultipartFile("image", "text.txt", MediaType.TEXT_PLAIN_VALUE, "plain text".getBytes());

    @Autowired
    public ImageFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    void getByAdvertisementId_shouldReturnAllImages() throws Exception {
        sendUriAndGetResultAction(get(IMAGE_RESOURCE, 1L), status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1]").value(Base64.encodeString(TEST_PNG)));
    }

    @Test
    void getImageLinksByAdvertisementId_shouldReturnAllImageLinks() throws Exception {
        sendUriAndGetResultAction(get(IMAGE_BY_ADV_ID, 1L), status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].resource").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].resource").value(Base64.encodeString(TEST_PNG)));
    }

    @ParameterizedTest
    @MethodSource("getAdvertisementIdAndExpectedResult")
    void countImagesInAdvertisement(long advertisementId, ResultMatcher expectedStatus, String expectedResponse) throws Exception {
        final var mvcResult = sendUriAndGetMvcResult(get(IMAGE_COUNT, advertisementId), expectedStatus);
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
    }

    private static Stream<Arguments> getAdvertisementIdAndExpectedResult() {
        return Stream.of(
                Arguments.of(1L, status().isOk(), "2"),
                Arguments.of(2L, status().isOk(), "0"),
                Arguments.of(50L, status().isNotFound(), "")
        );
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn415WhenNotSupportedType() throws Exception {
        sendUriAndGetMvcResult(multipart(IMAGE_BY_ADV_ID, 1L).file(txt), status().isUnsupportedMediaType());
    }

    @WithMockUser("admin")
    @Test
    @Commit
    @ExpectedDataSet(value = "image/delete.yml", ignoreCols = {"created", "updated"})
    void deleteImages_shouldDeleteMultipleImageWhenUserOwnsThemAll() throws Exception {
        sendUriAndGetMvcResult(delete(IMAGE_BY_ADV_ID, 1L)
                        .param("ids", "1", "2"),
                status().isOk());
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldSaveImage() throws Exception {
        var jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(Path.of("src/test/resources/image/test-image.jpeg")));
        mockMvc.perform(multipart(IMAGE_BY_ADV_ID, 1L)
                .file(jpeg)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
