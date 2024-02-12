package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.SUBCATEGORY_ID;
import static space.obminyashka.items_exchange.rest.api.ApiKey.SUBCATEGORY_NAMES;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SubcategoryFlowTest extends BasicControllerTest {

    public static final long SUBCATEGORY_ID_FOR_DELETING = 1L;
    public static final long EXISTENT_SUBCATEGORY_ID = 2L;
    public static final long EXISTENT_CATEGORY_ID = 1L;
    public static final long NONEXISTENT_CATEGORY_ID = 22222L;

    @Autowired
    public SubcategoryFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @ParameterizedTest
    @DataSet("database_init.yml")
    @MethodSource("subcategoryNameTestData")
    void getSubcategoryNamesByCategoryId_shouldResponseAccordingToIdCorrectness(long subcategoryId, ResultMatcher expectedResult) throws Exception {
        sendUriAndGetMvcResult(get(SUBCATEGORY_NAMES, subcategoryId), expectedResult);
    }

    private static Stream<Arguments> subcategoryNameTestData() {
        return Stream.of(
                Arguments.of(EXISTENT_CATEGORY_ID, status().isOk()),
                Arguments.of(NONEXISTENT_CATEGORY_ID, status().isNotFound())
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DataSet("database_init.yml")
    void deleteSubcategoryById_shouldDeleteExistedSubcategory() throws Exception {
        sendUriAndGetResultAction(delete(SUBCATEGORY_ID, EXISTENT_SUBCATEGORY_ID), status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DataSet("database_init.yml")
    void deleteSubcategoryById_whenSubcategoryHasAdvertisements_shouldReturnBadRequest() throws Exception {
        sendUriAndGetMvcResult(delete(SUBCATEGORY_ID, SUBCATEGORY_ID_FOR_DELETING), status().isBadRequest());
    }
}
