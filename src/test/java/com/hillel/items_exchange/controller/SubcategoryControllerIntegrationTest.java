package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
public class SubcategoryControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getSubcategoryNamesByCategoryId_shouldReturnAllSubcategoryNamesByCategoryIdIfExists() throws Exception {
        mockMvc.perform(get("/subcategory/{category_id}/names", 2L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"dolls\",\"puzzles\"]"));
    }

    @Test
    @DataSet("database_init.yml")
    public void getSubcategoryNamesByCategoryId_whenCategoryIdDoesNotExist_shouldReturnNotFound() throws Exception {
        long nonExistentSubcategoryId = 111111L;
        mockMvc.perform(get("/subcategory/{category_id}/names", nonExistentSubcategoryId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void deleteSubcategoryById_shouldDeleteExistedSubcategory() throws Exception {
        mockMvc.perform(delete("/subcategory/{subcategory_id}", 2L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DataSet("database_init.yml")
    public void deleteSubcategoryById_whenSubcategoryHasProducts_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/subcategory/{subcategory_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
