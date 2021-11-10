package space.obminyashka.items_exchange.i18n;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

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
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/image/{advertisement_id}", -100L)
                .header("Accept-Language", "notExisted")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("ID value has to be positive"));
    }

    @Test
    void testDefaultLocalizationWithoutAccept_LanguageHeader() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/image/{advertisement_id}", -100L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("ID value has to be positive"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru", "ru-RU"})
    void testRussianLocalizationWithAccept_LanguageHeaderIsRu(String locale) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/image/{advertisement_id}", -100L)
                .header("Accept-Language", locale)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8)
                .contains("Идентификатор должен быть положительным"));
    }
}
