package space.obminyashka.items_exchange.i18n;

import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import space.obminyashka.items_exchange.api.ApiKey;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InternationalizationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"notExisted", ""})
    @WithMockUser("junit")
    void testDefaultLocalizationAccordingToLanguageHeader(String languageHeader) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(ApiKey.USER_CHILD)
                        .header("Accept-Language", languageHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        final var errorMessage = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("User not found", new JSONObject(errorMessage).get("error"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ua", "ua-UA"})
    @WithMockUser("junit")
    void testUkrainianLocalizationWithAccept_LanguageHeaderIsUA(String locale) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(ApiKey.USER_CHILD)
                        .header("Accept-Language", locale)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        final var errorMessage = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("Користувача не знайдено", new JSONObject(errorMessage).get("error"));
    }
}
