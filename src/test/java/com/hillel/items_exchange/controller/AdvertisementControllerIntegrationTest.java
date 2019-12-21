package com.hillel.items_exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.database.rider.core.api.dataset.DataSet;
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

import static java.util.Collections.emptyList;
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
    void getAllAdvertisements_shouldReturnAllGenderedAdvertisements() throws Exception {
        mockMvc.perform(get("/adv/filtering/{gender}", "male")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
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
        ProductDto springDress = new ProductDto(1L, "16", "male", "spring", "40", lightShoes, emptyList());
        existDto = new AdvertisementDto(1L, "topic", "description", "shoes", true, true, DealType.EXCHANGE, kharkiv, springDress);
    }

    private void createNonExistAdvertisementDto() {
        LocationDto kharkiv = new LocationDto(0L, "Kharkiv", "District");
        CategoryDto clothes = new CategoryDto(0L, "Clothes");
        SubcategoryDto dress = new SubcategoryDto(0L, "dress", clothes);
        ProductDto springDress = new ProductDto(0L, "16", "male", "spring", "M", dress, emptyList());
        nonExistDto = new AdvertisementDto(0L, "topic", "description", "hat", false, false, DealType.GIVEAWAY, kharkiv, springDress);
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