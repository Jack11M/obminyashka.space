package com.hillel.items_exchange.exceptionshandling;

import com.hillel.items_exchange.controller.AdvertisementController;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.util.AdvertisementDtoCreatingUtil;
import com.hillel.items_exchange.util.MessageSourceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalControllerExceptionHandlerTest {

    private MockMvc mockMvc;
    private AdvertisementDto nonExistDto;

    @Mock
    AdvertisementController advertisementController;
    @Mock
    MessageSourceUtil messageSourceUtil;

    @BeforeEach
    void setup() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementDto();
        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController)
                .setControllerAdvice(new GlobalControllerExceptionHandler(messageSourceUtil))
                .build();
    }

    @Test
    public void testHandleUserNotFoundException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
