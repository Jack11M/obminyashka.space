package com.hillel.evoApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.evoApp.dto.AdvertisementDto;
import com.hillel.evoApp.model.*;
import com.hillel.evoApp.service.AdvertisementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdvertisementControllerTest {

    @Autowired
    private AdvertisementController advertisementController;
    @Mock
    private AdvertisementService advertisementService;
    private MockMvc mockMvc;
    private List<Advertisement> advertisementList;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController)
                .build();
        Advertisement one = new Advertisement("test_topic", DealType.EXCHANGE, true, "test_description", emptyList(), emptyList(), emptyList(), null, emptyList());
        Advertisement two = new Advertisement("second_topic", DealType.EXCHANGE, true, "second_description", emptyList(), emptyList(), emptyList(), null, emptyList());
        advertisementList = Arrays.asList(one, two);
    }

    @Test
    void getAllAdvertisements_shouldReturnAllAdvertisements() throws Exception {
        when(advertisementService.findAll()).thenReturn(advertisementList);
        mockMvc.perform(get("/adv/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    // todo: fix tests below
    @Test
    void getAdvertisement_shouldReturnAdvertisementIfExists() throws Exception {
        when(advertisementService.findById(anyLong())).thenReturn(Optional.of(advertisementList.get(0)));
        mockMvc.perform(get("/adv/{advertisement_id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.advertisement").exists())
                .andExpect(status().isOk());
    }

    @Test
    void createAdvertisement() throws Exception {
        mockMvc.perform(post("/employees")
                .content(asJsonString(createAdvertisementDto()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateAdvertisement() throws Exception {
        mockMvc.perform(put("/employees/{id}", 2)
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
        mockMvc.perform(delete("/employees/{id}", createAdvertisementDto()))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    private AdvertisementDto createAdvertisementDto() {
        return new AdvertisementDto("topic", DealType.EXCHANGE, false, "some description",
                new Location(), emptyList(), emptyList(), new Product(), new User());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}