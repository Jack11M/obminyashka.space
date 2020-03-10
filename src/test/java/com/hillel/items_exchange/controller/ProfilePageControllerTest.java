package com.hillel.items_exchange.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProfilePageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uponRequestProfile_redirectsToThePageProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name( "profile"));
    }

    @Test
    public void uponRequestProfileFavorites_redirectsToThePageFavorites() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/favorites"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("favorites"));
    }

    @Test
    public void uponRequestProfileSettings_redirectsToThePageSettings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/settings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("settings"));
    }

}