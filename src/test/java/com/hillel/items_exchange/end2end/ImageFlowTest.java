package com.hillel.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DataSet("database_init.yml")
class ImageFlowTest {

    private static final String TEST_JPEG = "test image jpeg";
    private static final String TEST_PNG = "test image png";
    @Autowired
    private MockMvc mockMvc;
    private MockMultipartFile txt;

    @BeforeEach
    void setUp() {
         txt = new MockMultipartFile("files", "text.txt", MediaType.TEXT_PLAIN_VALUE, "plain text".getBytes());
    }

    @Test
    void getByAdvertisementId_shouldReturnAllImages() throws Exception {
        mockMvc.perform(get("/image/{advertisement_id}/resource", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1]").value(Base64.encodeString(TEST_PNG)));
    }

    @Test
    void getImageLinksByAdvertisementId_shouldReturnAllImageLinks() throws Exception {
        mockMvc.perform(get("/image/{advertisement_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].resource").value(Base64.encodeString(TEST_JPEG)))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].resource").value(Base64.encodeString(TEST_PNG)));
    }

    @WithMockUser("admin")
    @Test
    void saveImages_shouldReturn415WhenNotSupportedType() throws Exception {
        mockMvc.perform(multipart("/image/{advertisement_id}", 1L)
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
