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
import space.obminyashka.items_exchange.api.ApiKey;

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
        MvcResult mvcResult = mockMvc.perform(get(ApiKey.USER_MY_INFO)
                .header("Accept-Language", "notExisted")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("You must be authenticated"));
    }

    @Test
    void testDefaultLocalizationWithoutAccept_LanguageHeader() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(ApiKey.USER_MY_INFO)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains("You must be authenticated"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ua", "ua-UA"})
    void testUkrainianLocalizationWithAccept_LanguageHeaderIsRu(String locale) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(ApiKey.USER_MY_INFO)
                .header("Accept-Language", locale)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8)
                .contains("Для доступа до поточного ресурса ви маєте авторизуватися"));
    }
}
