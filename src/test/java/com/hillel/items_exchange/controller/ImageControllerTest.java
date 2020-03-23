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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void getByAdvertisementIdAndProductId_shouldReturnAllImageUrls() throws Exception {
        mockMvc.perform(get("/adv/{advertisement_id}/product/{product_id}/images", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].resourceUrl").value("one"))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].resourceUrl").value("two"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getImageUrlsAdvertisementIdAndProductId_shouldReturnAllImageUrls() throws Exception {
        mockMvc.perform(get("/adv/{advertisement_id}/product/{product_id}/image-urls", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("one"))
                .andExpect(jsonPath("$[1]").value("two"));
    }
}