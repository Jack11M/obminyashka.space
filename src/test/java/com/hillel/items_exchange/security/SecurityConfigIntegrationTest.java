package com.hillel.items_exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.*;
import com.hillel.items_exchange.model.DealType;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import static com.hillel.items_exchange.utils.TestUtil.asJsonString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
public class SecurityConfigIntegrationTest {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "@kuIOIY*h986";

    private static final String NOT_VALID_USERNAME = "nimda";
    private static final String NOT_VALID_PASSWORD = "drowssap";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    private MockMvc mockMvc;
    private UserLoginDto validLoginDto;
    private UserLoginDto notValidLoginDto;
    private AdvertisementDto nonExistDto;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain)
                .apply(springSecurity()).build();

        createValidUserLoginDto();
        createNotValidUserLoginDto();
        createNonExistAdvertisementDto();
    }

    @Test
    @DataSet("database_init.yml")
    public void loginWithValidUserIsOk() throws Exception {
        mockMvc.perform(post("/auth/login")
                .content(asJsonString(validLoginDto))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DataSet("database_init.yml")
    public void loginWithNotValidUserIsOk() throws Exception {
        mockMvc.perform(post("/auth/login")
                .content(asJsonString(notValidLoginDto))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void createAdvertisementWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(post("/adv"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DataSet("database_init.yml")
    public void createAdvertisementWithValidTokenWithoutAdvertisementDtoIsBadRequest() throws Exception {
        final String token = obtainToken(validLoginDto);
        mockMvc.perform(post("/adv")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "security/create_for_security.yml", ignoreCols = {"created", "updated", "id",
                    "product_id", "subcategory_id", "category_id", "location_id"})
    public void createAdvertisementWithValidTokenAndValidAdvertisementDtoIsOk() throws Exception {
        final String token = obtainToken(validLoginDto);
        mockMvc.perform(post("/adv")
                .header("Authorization", "Bearer " + token)
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    private void createValidUserLoginDto() {
        validLoginDto = new UserLoginDto(VALID_USERNAME, VALID_PASSWORD);
    }

    private void createNotValidUserLoginDto() {
        notValidLoginDto = new UserLoginDto(NOT_VALID_USERNAME, NOT_VALID_PASSWORD);
    }

    private String extractToken(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    private String obtainToken(UserLoginDto loginDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                .content(asJsonString(loginDto))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json"))
                .andReturn();

        return extractToken(result);
    }

    private void createNonExistAdvertisementDto() {
        LocationDto lviv = new LocationDto(0L, "Lviv", "District");
        CategoryDto toys = new CategoryDto(0L, "Toys");
        SubcategoryDto smallToys = new SubcategoryDto(0L, "small_toys", toys);
        ProductDto toyTrain = new ProductDto(0L, "3", "male", "all", "M", smallToys,
                Collections.singletonList(new ImageDto(0L, "train_url", true)));
        nonExistDto = new AdvertisementDto(0L, "topic",
                "description", "hat",
                false, false, DealType.GIVEAWAY, lviv, toyTrain);
    }
}
