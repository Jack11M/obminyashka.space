package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
public class SubcategoryControllerIntegrationTest {

    public static final long SUBCATEGORY_ID_FOR_DELETING = 1L;
    public static final long EXISTENT_SUBCATEGORY_ID = 2L;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getSubcategoryNamesByCategoryId_shouldReturnAllSubcategoryNamesByCategoryIdIfExists() throws Exception {
        mockMvc.perform(get("/subcategory/{category_id}/names", EXISTENT_SUBCATEGORY_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"dolls\",\"puzzles\"]"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    @DataSet("database_init.yml")
    void deleteSubcategoryById_shouldDeleteExistedSubcategory() throws Exception {
        mockMvc.perform(delete("/subcategory/{subcategory_id}", EXISTENT_SUBCATEGORY_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DataSet("database_init.yml")
    public void deleteSubcategoryById_whenSubcategoryHasProducts_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/subcategory/{subcategory_id}", SUBCATEGORY_ID_FOR_DELETING)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
