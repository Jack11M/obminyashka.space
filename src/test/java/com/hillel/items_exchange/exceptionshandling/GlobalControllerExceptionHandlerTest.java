package com.hillel.items_exchange.exceptionshandling;

import com.hillel.items_exchange.controller.AdvertisementController;
import com.hillel.items_exchange.controller.CategoryController;
import com.hillel.items_exchange.controller.ImageController;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.util.AdvertisementDtoCreatingUtil;
import com.hillel.items_exchange.util.MessageSourceUtil;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalControllerExceptionHandlerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private AdvertisementDto nonExistDto;
    private AdvertisementDto existDto;

    @Mock
    AdvertisementController advertisementController;

    @Mock
    CategoryController categoryController;

    @Mock
    ImageController imageController;

    @Mock
    MessageSourceUtil messageSourceUtil;

    @BeforeEach
    void setup() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementDto();
        existDto = AdvertisementDtoCreatingUtil.createExistAdvertisementDto();
        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController, categoryController)
                .setControllerAdvice(new GlobalControllerExceptionHandler(messageSourceUtil))
                .build();
    }

    @Test
    public void testHandleUserNotFoundException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(UsernameNotFoundException.class);

        MvcResult result = mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(UsernameNotFoundException.class)));
    }

    @Test
    public void testHandleSecurityException() throws Exception {
        when(advertisementController.updateAdvertisement(any(), any())).thenThrow(SecurityException.class);

        MvcResult result = mockMvc.perform(put("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(SecurityException.class)));
    }

    @Test
    public void testHandleEntityNotFoundException() throws Exception {
        when(categoryController.getCategoryById(anyLong())).thenThrow(EntityNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/category/{category_id}", -1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(EntityNotFoundException.class)));
    }

    @Test
    public void testHandleIllegalIdentifierException() throws Exception {
        when(advertisementController.updateAdvertisement(any(), any())).thenThrow(IllegalIdentifierException.class);

        MvcResult result = mockMvc.perform(put("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    public void testHandleInvalidDtoException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(InvalidDtoException.class);

        MvcResult result = mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(InvalidDtoException.class)));
    }

    @Test
    public void testHandleSqlException() throws Exception {
        when(advertisementController.updateAdvertisement(any(), any())).thenThrow(DataIntegrityViolationException.class);

        MvcResult result = mockMvc.perform(put("/adv")
                .content(asJsonString(existDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(DataIntegrityViolationException.class)));
    }

    @Test
    public void testHandleIllegalArgumentException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(IllegalArgumentException.class)));
    }

    @Test
    public void testHandleConstraintViolationException() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        MvcResult result = mockMvc.perform(get("/image/{product_id}", -1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(ConstraintViolationException.class)));
    }
}
