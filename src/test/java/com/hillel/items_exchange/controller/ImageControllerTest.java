package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getImageUrlsByProductId_shouldReturnAllImageUrls() throws Exception {
        mockMvc.perform(get("/products/1/imageUrls")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getImagesByProductId_shouldReturnAllImages() throws Exception {
        mockMvc.perform(get("/products/1/images")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}