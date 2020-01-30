package com.hillel.items_exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.*;
import com.hillel.items_exchange.model.DealType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class AdvertisementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    private AdvertisementDto nonExistDto;
    private AdvertisementDto existDto;


    @BeforeEach
    void setUp() {
        createNonExistAdvertisementDto();
        createExistAdvertisementDto();
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAllAdvertisements_shouldReturnAllAdvertisements() throws Exception {
        mockMvc.perform(get("/adv")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
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
                .andExpect(status().isFound());
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
        existDto.setDescription("new description");
        existDto.setTopic("new topic");
        existDto.setWishesToExchange("BMW");

        mockMvc.perform(put("/adv")
                .content(asJsonString(existDto))
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

    private void createExistAdvertisementDto() {
        LocationDto kharkiv = new LocationDto(1L, "Kharkiv", "Kharkivska district");
        CategoryDto shoes = new CategoryDto(1L, "shoes");
        SubcategoryDto lightShoes = new SubcategoryDto(1L, "light_shoes", shoes);
        ProductDto springDress = new ProductDto(1L, "16", "male", "spring", "40", lightShoes,
                Arrays.asList(new ImageDto(1L, "one", false), new ImageDto(2L, "two", true)));
        existDto = new AdvertisementDto(1L, "topic", "description", "shoes", true, true, DealType.EXCHANGE, kharkiv, springDress);
    }

    private void createNonExistAdvertisementDto() {
        LocationDto kyiv = new LocationDto(0L, "Kyiv", "District");
        CategoryDto clothes = new CategoryDto(0L, "Clothes");
        SubcategoryDto dress = new SubcategoryDto(0L, "dress", clothes);
        ProductDto springDress = new ProductDto(0L, "16", "male", "spring", "M", dress,
                Collections.singletonList(new ImageDto(0L, "url", false)));
        nonExistDto = new AdvertisementDto(0L, "topic", "description", "hat", false, false, DealType.GIVEAWAY, kyiv, springDress);
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}