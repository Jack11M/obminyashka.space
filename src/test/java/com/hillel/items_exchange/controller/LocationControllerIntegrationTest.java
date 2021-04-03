package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static com.hillel.items_exchange.util.LocationDtoCreatingUtil.*;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class LocationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Value("${test.data.location.init.file.path}")
    private String pathToFileParseLocationsFrom;
    @Value("${location.init.file.path}")
    private String pathToCreateLocationsInitFile;

    @Test
    @DataSet("database_init.yml")
    void getAllLocations_shouldReturnAllLocations() throws Exception {
        mockMvc.perform(get("/location"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].city").value("Kharkiv"))
                .andExpect(jsonPath("$[0].district").value("Kharkivska district"));
    }

    @Test
    @DataSet("database_init.yml")
    void getLocationsForCurrentLanguage_shouldReturnLocations() throws Exception {
        mockMvc.perform(get("/location/all").header("accept-language" , "en"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].city").value("Kharkiv"))
                .andExpect(jsonPath("$[0].district").value("Kharkivska district"));
    }

    @Test
    @DataSet("database_init.yml")
    void getLocationsForCurrentLanguage_shouldReturn404WhenLocationsForLangDoNotExist() throws Exception {
        mockMvc.perform(get("/location/all").header("accept-language" , "ua"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet("database_init.yml")
    void getLocation_shouldReturnLocationWithGivenId() throws Exception {
        mockMvc.perform(get("/location/{location_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.city").value("Kharkiv"))
                .andExpect(jsonPath("$.district").value("Kharkivska district"));
    }

    @Test
    void getLocation_shouldReturn404WhenLocationIsNotExisted() throws Exception {
        mockMvc.perform(get("/location/{location_id}", 555L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "admin")
    @Test
    void createLocation_shouldReturn409WhenUserHasNotRoleAdmin() throws Exception {
        mockMvc.perform(post("/location")
                .content(asJsonString(createLocationDtoWithId(0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createLocation_shouldReturn400WhenLocationIsNotZero() throws Exception {
        mockMvc.perform(post("/location")
                .content(asJsonString(createLocationDtoWithId(1L)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createLocation_shouldReturn400WhenInvalidCity() throws Exception {
        mockMvc.perform(post("/location")
                .content(asJsonString(createLocationDtoForCreatingWithInvalidCity()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("location/create.yml")
    void createLocation_shouldCreateNewLocation() throws Exception {
        mockMvc.perform(post("/location")
                .content(asJsonString(createLocationDtoWithId(0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.city").value(NEW_VALID_CITY))
                .andExpect(jsonPath("$.district").value(NEW_VALID_DISTRICT))
                .andReturn();
    }

    @WithMockUser(username = "admin")
    @Test
    void updateLocation_shouldReturn409WhenUserHasNotRoleAdmin() throws Exception {
        mockMvc.perform(put("/location")
                .content(asJsonString(createLocationDtoWithId(0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateLocation_shouldReturn404WhenLocationIsNotExisted() throws Exception {
        mockMvc.perform(put("/location")
                .content(asJsonString(createLocationDtoWithId(555L)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("location/update.yml")
    void updateLocation_shouldUpdateExistedLocation() throws Exception {
        mockMvc.perform(put("/location")
                .content(asJsonString(createLocationDtoWithId(1L)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.city").value(NEW_VALID_CITY))
                .andExpect(jsonPath("$.district").value(NEW_VALID_DISTRICT))
                .andReturn();
    }

    @WithMockUser(username = "admin")
    @Test
    void deleteLocations_shouldReturn409WhenUserHasNotRoleAdmin() throws Exception {
        mockMvc.perform(delete("/location")
                .param("ids", "1"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteLocations_shouldReturn400WhenLocationIdIsNotExisted() throws Exception {
        mockMvc.perform(delete("/location")
                .param("ids", "1", "ids", "555"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("location/delete.yml")
    void deleteLocations_shouldDeleteExistedLocation() throws Exception {
        mockMvc.perform(delete("/location")
                .param("ids", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    void createLocationsInitFile_whenDataIsValid_shouldCreateFileAndReturnItsContent() throws Exception {
        MvcResult response = mockMvc.perform(post("/location/locations-init")
                .content(Files.readString(Path.of(pathToFileParseLocationsFrom), StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String[] parsedLocationsQuantity = response.getResponse().getContentAsString().split("\\), \\(");
        int lastId = Integer.parseInt(parsedLocationsQuantity[parsedLocationsQuantity.length - 1].substring(1, 5));
        int locationsInThreeLanguages = (response.getResponse().getContentAsString().substring(88).split("\\),").length)/3;
        assertEquals(locationsInThreeLanguages * 3, lastId);
        assertTrue(Files.size(Path.of(pathToCreateLocationsInitFile)) > 0);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    void createLocationsInitFile_whenDataIsNotValid_shouldReturnBadRequestAndProperMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/location/locations-init")
                .content("NOT VALID DATA"))
                .andDo(print()).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        String stringToSearch = getMessageSource("exception.invalid.locations.file.creating.data");
        assertTrue(mvcResult.getResponse().getContentAsString().contains(stringToSearch));
    }
}
