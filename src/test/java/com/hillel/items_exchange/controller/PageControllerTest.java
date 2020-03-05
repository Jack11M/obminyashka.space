package com.hillel.items_exchange.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uponRequestTemp_redirectsToThePageTemp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/temp"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("temp"));
    }

    @Test
    public void uponRequestRegistration_redirectsToThePageRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void uponRequestSearch_redirectsToThePageResult() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("result"));
    }

    @Test
    public void uponRequestProductCreate_redirectsToThePageCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("create"));
    }

    @Test
    public void uponRequestProductInfo_redirectsToThePageInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("info"));
    }

    @Test
    public void uponRequestProfile_redirectsToThePageProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
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
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/settings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("settings"));
    }
}
