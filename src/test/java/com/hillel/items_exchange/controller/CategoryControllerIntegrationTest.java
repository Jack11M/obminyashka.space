package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.dto.SubcategoryDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.hillel.items_exchange.util.TestUtil.asJsonString;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
public class CategoryControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAllCategoriesNames_shouldReturnAllCategoriesNames() throws Exception {
        mockMvc.perform(get("/category/names")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAllCategories_shouldReturnAllCategories() throws Exception {
        mockMvc.perform(get("/category/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getCategoryById_shouldReturnCategoryByIdIfExists() throws Exception {
        mockMvc.perform(get("/category/{category_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("shoes"))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet("database_init.yml")
    void getCategoryById_whenCategoryIdDoesNotExist_shouldReturnNotFoundAndThrowEntityNotFoundException() throws Exception {
        long nonExistentCategoryId = 100000000L;

        MvcResult result = this.mockMvc.perform(get("/category/{category_id}", nonExistentCategoryId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        Optional<EntityNotFoundException> entityNotFoundException =
                Optional.ofNullable((EntityNotFoundException) result.getResolvedException());

        entityNotFoundException.ifPresent((e) -> assertThat(e, is(notNullValue())));
        entityNotFoundException.ifPresent((e) -> assertThat(e, is(instanceOf(EntityNotFoundException.class))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "category/create_category.yml")
    void createCategory_shouldCreateValidCategory() throws Exception {
        CategoryDto nonExistCategoryDto = createNonExistValidCategoryDto();
        mockMvc.perform(post("/category")
                .content(asJsonString(nonExistCategoryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DataSet("database_init.yml")
    void createCategory_whenUserDoesNotHaveRoleAdmin_shouldReturnUnauthorized() throws Exception {
        CategoryDto nonExistCategoryDto = createNonExistValidCategoryDto();
        mockMvc.perform(post("/category")
                .content(asJsonString(nonExistCategoryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void createCategory_whenCategoryIdNotEqualsZero_shouldReturnBadRequestAndThrowInvalidDtoException()
            throws Exception {
        CategoryDto nonExistCategoryDtoWithInvalidId = createNonExistCategoryDtoWithInvalidId();
        String errorMessage = "New category or subcategory mustn't contain id except 0, " +
                "also category name mustn't have duplicates!";

        MvcResult result = mockMvc.perform(post("/category")
                .content(asJsonString(nonExistCategoryDtoWithInvalidId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)))
                .andReturn();

        Optional<InvalidDtoException> invalidDtoException =
                Optional.ofNullable((InvalidDtoException) result.getResolvedException());

        invalidDtoException.ifPresent((e) -> assertThat(e, is(notNullValue())));
        invalidDtoException.ifPresent((e) -> assertThat(e, is(instanceOf(InvalidDtoException.class))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void createCategory_whenInternalSubcategoryIdNotEqualsZero_shouldReturnBadRequest()
            throws Exception {
        CategoryDto nonExistCategoryDtoWithInvalidId = createNonExistCategoryDtoWithInvalidSubcategoryId();

        mockMvc.perform(post("/category")
                .content(asJsonString(nonExistCategoryDtoWithInvalidId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void createCategory_whenCategoryNameHasDuplicate_shouldReturnBadRequest()
            throws Exception {
        CategoryDto nonExistCategoryDtoWithDuplicateName = createNonExistCategoryDtoWithDuplicateName();

        mockMvc.perform(post("/category")
                .content(asJsonString(nonExistCategoryDtoWithDuplicateName))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "category/update_category.yml")
    void updateCategory_shouldUpdateExistedCategory() throws Exception {
        CategoryDto existCategoryDto = createExistCategoryDto();
        existCategoryDto.setSubcategories(Arrays.asList(new SubcategoryDto(1L, "light shoes"),
                new SubcategoryDto(0L, "winter shoes")));
        existCategoryDto.setName("footwear");
        mockMvc.perform(put("/category")
                .content(asJsonString(existCategoryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("footwear"))
                .andExpect(jsonPath("$.subcategories", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void updateCategory_whenCategoryIdDoesNotExist_shouldReturnBadRequestAndThrowIllegalIdentifierException()
            throws Exception {
        long nonExistentCategoryId = 100000001L;
        CategoryDto existCategoryDto = createExistCategoryDto();
        existCategoryDto.setSubcategories(Arrays.asList(new SubcategoryDto(1L, "men shoes"),
                new SubcategoryDto(0L, "women shoes")));
        existCategoryDto.setId(nonExistentCategoryId);

        String errorMessage = "The updated category and subcategories have " +
                "to exist by id, new subcategories have to be 0 and also category name mustn't have duplicates " +
                "except current name!";

        MvcResult result = mockMvc.perform(put("/category")
                .content(asJsonString(existCategoryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)))
                .andReturn();

        Optional<IllegalIdentifierException> illegalIdentifierException =
                Optional.ofNullable((IllegalIdentifierException) result.getResolvedException());

        illegalIdentifierException.ifPresent((e) -> assertThat(e, is(notNullValue())));
        illegalIdentifierException.ifPresent((e) -> assertThat(e, is(instanceOf(IllegalIdentifierException.class))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void updateCategory_whenSubcategoryIdDoesNotExistAndNotEqualsZero_shouldReturnBadRequest()
            throws Exception {
        long nonExistentSubcategoryId = 111111L;
        CategoryDto existCategoryDto = createExistCategoryDto();
        existCategoryDto.setSubcategories(Arrays.asList(new SubcategoryDto(nonExistentSubcategoryId, "men shoes"),
                new SubcategoryDto(0L, "women shoes")));
        existCategoryDto.setName("new name");

        mockMvc.perform(put("/category")
                .content(asJsonString(existCategoryDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void deleteCategoryById_shouldDeleteExistedCategory() throws Exception {
        mockMvc.perform(delete("/category/{category_id}", 2L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void deleteCategory_whenCategoryIdDoesNotExist_shouldReturnBadRequestAndThrowInvalidDtoException()
            throws Exception {
        long nonExistentCategoryId = 100000021L;
        String errorMessage = "The category can not be deleted by this id, " +
                "because it has to exist by id and it's subcategories mustn't have products! Given category id: ";

        MvcResult result = this.mockMvc.perform(delete("/category/{category_id}", nonExistentCategoryId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)))
                .andReturn();

        Optional<InvalidDtoException> invalidDtoException =
                Optional.ofNullable((InvalidDtoException) result.getResolvedException());

        invalidDtoException.ifPresent((e) -> assertThat(e, is(notNullValue())));
        invalidDtoException.ifPresent((e) -> assertThat(e, is(instanceOf(InvalidDtoException.class))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void deleteCategory_whenInternalSubcategoryHasProducts_shouldReturnBadRequest()
            throws Exception {
        mockMvc.perform(delete("/category/{category_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private CategoryDto createExistCategoryDto() {
        SubcategoryDto lightShoes = new SubcategoryDto(1L, "light_shoes");
        return new CategoryDto(1L, "shoes", Collections.singletonList(lightShoes));
    }

    private CategoryDto createNonExistValidCategoryDto() {
        SubcategoryDto fairyTales = new SubcategoryDto(0L, "fairy tales");
        SubcategoryDto educationalBooks = new SubcategoryDto(0L, "educational books");
        return new CategoryDto(0L, "books", Arrays.asList(fairyTales, educationalBooks));
    }

    private CategoryDto createNonExistCategoryDtoWithInvalidId() {
        SubcategoryDto validSubcategory = new SubcategoryDto(0L, "valid subcategory");
        return new CategoryDto(1L, "invalid", Collections.singletonList(validSubcategory));
    }

    private CategoryDto createNonExistCategoryDtoWithInvalidSubcategoryId() {
        SubcategoryDto validSubcategory = new SubcategoryDto(0L, "valid subcategory");
        SubcategoryDto invalidSubcategory = new SubcategoryDto(1L, "invalid subcategory");
        return new CategoryDto(0L, "invalid category", Arrays.asList(validSubcategory, invalidSubcategory));
    }

    private CategoryDto createNonExistCategoryDtoWithDuplicateName() {
        SubcategoryDto validSubcategory = new SubcategoryDto(0L, "valid subcategory");
        SubcategoryDto otherValidSubcategory = new SubcategoryDto(0L, "valid subcategory");
        return new CategoryDto(0L, "shoes", Arrays.asList(validSubcategory, otherValidSubcategory));
    }
}
