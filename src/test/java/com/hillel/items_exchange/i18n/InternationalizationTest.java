package com.hillel.items_exchange.i18n;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InternationalizationTest {

@Autowired
    private MockMvc mockMvc;

    @Test
    void testDefaultLocalizationWithNotExistedAccept_LanguageHeader() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/image/{product_id}", -100L)
                .header("Accept-Language", "notExisted")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("Id value has to be 0 or positive"));
    }

    @Test
    void testDefaultLocalizationWithoutAccept_LanguageHeader() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/image/{product_id}", -100L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("Id value has to be 0 or positive"));
    }

    @Test
    void testRussianLocalizationWithAccept_LanguageHeaderIsRu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/image/{product_id}", -100L)
                .header("Accept-Language", "ru")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("Идентификатор не может быть отрицательным"));
    }

    @Test
    void testRussianLocalizationWithAccept_LanguageHeaderIsRu_Ru() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/image/{product_id}", -100L)
                .header("Accept-Language", "ru-Ru")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("Идентификатор не может быть отрицательным"));
    }
}
