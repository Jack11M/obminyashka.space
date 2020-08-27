package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.ProductDto;
import com.hillel.items_exchange.util.AdvertisementDtoCreatingUtil;
import com.hillel.items_exchange.util.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Collections;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class AdvertisementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    private AdvertisementDto nonExistDto;
    private AdvertisementDto existDto;
    private AdvertisementDto existDtoForUpdate;
    private int page, size;
    private long validId;
    private long notValidId;

    @BeforeEach
    void setUp() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementDto();
        existDto = AdvertisementDtoCreatingUtil.createExistAdvertisementDto();
        validId = 1L;
        notValidId =999L;
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void findPaginated_shouldReturnSelectedQuantity() throws Exception {
        setPageAndSize(0, 2);
        MvcResult mvcResult = mockMvc.perform(get("/adv?page={page}&size={size}", page, size)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        AdvertisementDto[] advertisementsDtos = JsonConverter.jsonToObject(json, AdvertisementDto[].class);
        assertEquals(size, advertisementsDtos.length);
    }

    @Test
    void findPaginated_shouldBeThrownValidationException() throws Exception {
        setPageAndSize(0, -12);
        MvcResult mvcResult = mockMvc.perform(get("/adv?page={page}&size={size}", page, size))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("must be greater than or equal to 0"));
    }

    private void setPageAndSize(int page, int size) {
        this.page = page;
        this.size = size;
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAllAdvertisements_shouldReturnAdvertisementsByTopic() throws Exception {
        mockMvc.perform(get("/adv/topic/{topic}", "ses")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].topic").value("Blouses"))
                .andExpect(jsonPath("$[1].topic").value("Dresses"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementIfExists() throws Exception {
        mockMvc.perform(get("/adv/{advertisement_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("topic"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementsIfAnyValueExists() throws Exception {
        ProductDto productDto = new ProductDto(0L, "16", "male", "spring", "XL", 2L,
                Collections.emptyList());

        mockMvc.perform(post("/adv/filter")
                .content(asJsonString(productDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/create.yml", ignoreCols = {"created", "updated"})
    void createAdvertisement_shouldCreateValidAdvertisement() throws Exception {
        mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/update.yml", ignoreCols = "updated")
    void updateAdvertisement_shouldUpdateExistedAdvertisement() throws Exception {
        existDtoForUpdate = AdvertisementDtoCreatingUtil
                .createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory();

        mockMvc.perform(put("/adv")
                .content(asJsonString(existDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.description").value("new description"))
                .andExpect(jsonPath("$.topic").value("new topic"))
                .andExpect(jsonPath("$.wishesToExchange").value("BMW"));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/delete.yml")
    void deleteAdvertisement_shouldDeleteExistedAdvertisement() throws Exception {
        mockMvc.perform(delete("/adv")
                .content(asJsonString(existDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/setDefaultImage.yml")
    void setDefaultImage_success() throws Exception {
        mockMvc.perform(post("/adv/default-image/{advertisementId}/{imageId}", validId, validId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void setDefaultImage_shouldReturn406WhenNotValidAdvertisementId() throws Exception {
        mockMvc.perform(
                post("/adv/default-image/{advertisementId}/{imageId}", notValidId, validId))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void setDefaultImage_shouldReturn406WhenNotValidImageId() throws Exception {
        mockMvc.perform(
                post("/adv/default-image/{advertisementId}/{imageId}", validId, notValidId))
                .andExpect(status().isNotAcceptable())
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
}