package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.exception.InvalidDtoException;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class CategoryFlowTest extends BasicControllerTest {

    @Autowired
    public CategoryFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @DataSet("database_init.yml")
    void getAllCategoriesNames_shouldReturnAllCategoriesName() throws Exception {
        sendUriAndGetMvcResult(get(CATEGORY_NAMES), status().isOk());
    }

    @Test
    @DataSet("database_init.yml")
    void getAllCategories_shouldReturnAllCategoriesDto() throws Exception {
        sendUriAndGetMvcResult(get(CATEGORY_ALL), status().isOk());
    }

    @Test
    @DataSet("database_init.yml")
    void getCategoryById_shouldReturnCategoryByIdIfExists() throws Exception {
        sendUriAndGetResultAction(get(CATEGORY_ID, EXISTING_ENTITY_ID), status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("shoes"));
    }

    @Test
    @DataSet("database_init.yml")
    void getCategoryById_whenCategoryIdDoesNotExist_shouldReturnNotFound() throws Exception {
        sendUriAndGetMvcResult(get(CATEGORY_ID, NONEXISTENT_ENTITY_ID), status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("getTestCategoryValues")
    void getCategorySizesById_shouldReturnValues_whenIdMatches(int categoryId, ResultMatcher expectedStatus, int expectedSize, String firstElement) throws Exception {
        final var mvcResult = sendUriAndGetResultAction(get(CATEGORY_SIZES, categoryId), expectedStatus)
                .andExpect(jsonPath("$.length()").value(expectedSize))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains(firstElement));
    }

    private static Stream<Arguments> getTestCategoryValues() {
        return Stream.of(
                Arguments.of(1, status().isOk(), 21, "46 - 50"),
                Arguments.of(2, status().isOk(), 38, "9.5")
        );
    }

    @Test
    void createCategory_whenUserDoesNotHaveRoleAdmin_shouldReturnForbidden() throws Exception {
        CategoryDto nonExistCategoryDto = createNonExistValidCategoryDto();
        sendDtoAndGetMvcResult(post(CATEGORY), nonExistCategoryDto, status().isForbidden());
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @Commit
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "category/create_category.yml")
    void createCategory_shouldCreateValidCategory() throws Exception {
        CategoryDto nonExistCategoryDto = createNonExistValidCategoryDto();

        sendDtoAndGetResultAction(post(CATEGORY), nonExistCategoryDto, status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "category/update_category.yml")
    void updateCategory_shouldUpdateExistedCategoryWithAllReceivedSubcategories() throws Exception {
        CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID, EXISTING_ENTITY_ID, "footwear");

        sendDtoAndGetResultAction(put(CATEGORY_ID, updatedCategoryDto.getId()), updatedCategoryDto, status().isAccepted())
                .andExpect(jsonPath("$.name").value("footwear"))
                .andExpect(jsonPath("$.subcategories", hasSize(3)));
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @DataSet("database_init.yml")
    void updateCategory_whenCategoryIdDoesNotExist_shouldReturnBadRequestAndThrowIllegalIdentifierException() throws Exception {
        CategoryDto updatedCategoryDto = getUpdatedCategoryDto(EXISTING_ENTITY_ID, NONEXISTENT_ENTITY_ID, NEW_CATEGORY_NAME);

        MvcResult result = sendDtoAndGetMvcResult(put(CATEGORY_ID, updatedCategoryDto.getId()), updatedCategoryDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @DataSet("database_init.yml")
    void updateCategory_shouldUpdateSubcategory_whenValid() throws Exception {
        CategoryDto updatedCategoryDto = getUpdatedCategoryDto(NONEXISTENT_ENTITY_ID, EXISTING_ENTITY_ID, NEW_CATEGORY_NAME);

        sendDtoAndGetMvcResult(put(CATEGORY_ID, updatedCategoryDto.getId()), updatedCategoryDto, status().isAccepted());
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @DataSet("database_init.yml")
    void deleteCategoryById_shouldDeleteExistedCategory() throws Exception {
        sendUriAndGetResultAction(delete(CATEGORY_ID, 2L), status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @DataSet("database_init.yml")
    void deleteCategory_whenCategoryIdDoesNotExist_shouldReturnBadRequestAndThrowInvalidDtoException() throws Exception {
        MvcResult result = sendUriAndGetMvcResult(delete(CATEGORY_ID, NONEXISTENT_ENTITY_ID), status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(InvalidDtoException.class)));
    }

    @Test
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    @DataSet("database_init.yml")
    void deleteCategory_whenInternalSubcategoryHasAdvertisements_shouldReturnBadRequest() throws Exception {
        sendUriAndGetMvcResult(delete(CATEGORY_ID, EXISTING_ENTITY_ID), status().isBadRequest());
    }
}
