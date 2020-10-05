package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.dbunit.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DataSet("database_init.yml")
class ImageControllerIntegrationTest {
    private static final String TEST_JPEG = "test image jpeg";
    private static final String TEST_PNG = "test image png";
    @Autowired
    private MockMvc mockMvc;
    private MockMultipartFile jpeg;
    private MockMultipartFile txt;

    @BeforeEach
    void setUp() {
         jpeg = new MockMultipartFile("files", "image-jpeg.jpeg", MediaType.IMAGE_JPEG_VALUE, "image jpeg".getBytes());
         txt = new MockMultipartFile("files", "text.txt", MediaType.TEXT_PLAIN_VALUE, "plain text".getBytes());
    }

    @Test
    @Transactional
    void getByProductId_shouldReturnAllImages() throws Exception {
        mockMvc.perform(get("/image/{product_id}/resource", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1]").value(Base64.encodeString(TEST_PNG)));
    }

    @Test
    @Transactional
    void getImageLinksByProductId_shouldReturnAllImageLinks() throws Exception {
        mockMvc.perform(get("/image/{product_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].resource").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[0].defaultPhoto").value(false))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].resource").value(Base64.encodeString(TEST_PNG)))
                .andExpect(jsonPath("$[1].defaultPhoto").value(true));
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn400WhenProductIsNotExist() throws Exception {
        mockMvc.perform(multipart("/image/{product_id}", 50L)
                .file(jpeg))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn415WhenNotSupportedType() throws Exception {
        mockMvc.perform(multipart("/image/{product_id}", 1L)
                .file(txt))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @WithMockUser("admin")
    @Test
    @ExpectedDataSet(value = "image/delete.yml", ignoreCols = {"created", "updated"})
    void deleteImages_shouldDeleteMultipleImageWhenUserOwnsThemAll() throws Exception {
        mockMvc.perform(delete("/image/{advertisement_id}", 1)
                .param("ids", "1", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}