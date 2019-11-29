package com.hillel.items_exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hillel.items_exchange.dto.*;
import com.hillel.items_exchange.model.DealType;
import com.hillel.items_exchange.service.AdvertisementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AdvertisementControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdvertisementService advertisementService;
    private List<AdvertisementDto> advertisementList;


    @BeforeEach
    void setUp() {
        LocationDto kharkiv = new LocationDto(null, "Kharkiv", "District");
        CategoryDto clothes = new CategoryDto(null, "Clothes");
        SubcategoryDto dress = new SubcategoryDto(null, "dress", clothes);
        ProductDto springDress = new ProductDto(null, "16", "male", "spring", "M", dress, emptyList());
        UserDto stas = new UserDto(null, "user", "123456", "user@mail.ua", "Kharkiv", true, "super", "user", "", LocalDate.now());

        AdvertisementDto one = new AdvertisementDto(1L,"test_topic", "description one", "boots, dress, hat", true, false, DealType.EXCHANGE, kharkiv, springDress, stas);
        AdvertisementDto two = new AdvertisementDto(2L,"test_topic 2", "description two", "boots, dress, hat", true, false, DealType.EXCHANGE, kharkiv, springDress, stas);
        advertisementList = Arrays.asList(one, two);
    }

    @Test
    void getAllAdvertisements_shouldReturnAllAdvertisements() throws Exception {
        when(advertisementService.findAll()).thenReturn(advertisementList);
        mockMvc.perform(get("/adv")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void getAdvertisement_shouldReturnAdvertisementIfExists() throws Exception {
        when(advertisementService.findById(1L)).thenReturn(Optional.of(advertisementList.get(0)));
        mockMvc.perform(get("/adv/{advertisement_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("test_topic"))
                .andExpect(status().isOk());
    }

    // todo: fix

    @Test
    void createAdvertisement() throws Exception {
        AdvertisementDto dto = advertisementList.get(0);
        when(advertisementService.createAdvertisement(dto)).thenReturn(advertisementList.get(0));

        mockMvc.perform(post("/adv", dto)
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateAdvertisement() throws Exception {
        mockMvc.perform(put("/adv", createAdvertisementDto())
                .content(asJsonString(createAdvertisementDto()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("second_topic"))
                .andExpect(jsonPath("$.isFavourite").value("true"))
                .andExpect(jsonPath("$.description").value("second_description"));
    }

    @Test
    void deleteAdvertisement() throws Exception {
        mockMvc.perform(delete("/adv", createAdvertisementDto()))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    private AdvertisementDto createAdvertisementDto() {
        return null;
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