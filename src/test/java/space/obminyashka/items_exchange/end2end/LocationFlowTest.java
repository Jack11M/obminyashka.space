package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import space.obminyashka.items_exchange.BasicControllerTest;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.*;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class LocationFlowTest extends BasicControllerTest {

    @Value("${test.data.location.init.file.path}")
    private String pathToFileParseLocationsFrom;
    @Value("${location.init.file.path}")
    private String pathToCreateLocationsInitFile;

    private final String existedLocationId = "2c5467f3-b7ee-48b1-9451-7028255b757b";
    @Autowired
    public LocationFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @DataSet("database_init.yml")
    void getAllLocations_shouldReturnAllLocations() throws Exception {
        sendUriAndGetResultAction(get(LOCATION_ALL), status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(existedLocationId))
                .andExpect(jsonPath("$[0].cityEN").value("Kharkiv"))
                .andExpect(jsonPath("$[0].districtEN").value("Kharkivska district"));
    }

    @Test
    @DataSet("database_init.yml")
    void getLocationsForCurrentLanguage_shouldReturnLocations() throws Exception {
        sendUriAndGetResultAction(get(LOCATION_ALL), status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(existedLocationId))
                .andExpect(jsonPath("$[0].cityEN").value("Kharkiv"))
                .andExpect(jsonPath("$[0].districtEN").value("Kharkivska district"));
    }

    @Test
    @DataSet("database_init.yml")
    void getLocation_shouldReturnLocationWithGivenId() throws Exception {
        sendUriAndGetResultAction(get(LOCATION_ID, existedLocationId), status().isOk())
                .andExpect(jsonPath("$.id").value(existedLocationId))
                .andExpect(jsonPath("$.cityEN").value("Kharkiv"))
                .andExpect(jsonPath("$.districtEN").value("Kharkivska district"));
    }

    @Test
    void getLocation_shouldReturn404WhenLocationIsNotExisted() throws Exception {
        sendUriAndGetMvcResult(get(LOCATION_ID, UUID.randomUUID().toString()), status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "location/create.yml", orderBy = "city_en", ignoreCols = "id")
    void createLocation_shouldCreateNewLocation() throws Exception {
        sendAndCompareLocationResponse(post(LOCATION), status().isCreated());
    }

    private void sendAndCompareLocationResponse(MockHttpServletRequestBuilder request, ResultMatcher created) throws Exception {
        sendDtoAndGetResultAction(request, createValidLocationDto(), created)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cityEN").value(CITY_EN))
                .andExpect(jsonPath("$.districtEN").value(DISTRICT_EN))
                .andExpect(jsonPath("$.areaEN").value(AREA_EN))
                .andExpect(jsonPath("$.cityUA").value(CITY_UA))
                .andExpect(jsonPath("$.districtUA").value(DISTRICT_UA))
                .andExpect(jsonPath("$.areaUA").value(AREA_UA))
                .andReturn();
    }
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "location/create.yml", orderBy = "city_en", ignoreCols = "id")
    void createTwoSameLocation_shouldGetMessageBadRequest() throws Exception
    {
        sendAndCompareLocationResponse(post(LOCATION), status().isCreated());
        sendDtoAndGetResultAction(post(LOCATION), createValidLocationDto(), status().isBadRequest());
    }
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = { "location/createTwoLocationWithDifferentArea.yml" }, orderBy = "area_en", ignoreCols = "id")
    void createTwoDifferentAreaLocation_shouldCreateNewLocation() throws Exception
    {
        sendAndCompareLocationResponse(post(LOCATION), status().isCreated());
        var secondLocation = createValidLocationDto();
        secondLocation.setAreaUA("Харківська область");
        secondLocation.setAreaEN("Kharkiv area");
        sendDtoAndGetResultAction(post(LOCATION), secondLocation, status().isCreated());
    }
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = { "location/createTwoLocationWithDifferentCity.yml" }, orderBy = "city_en", ignoreCols = "id")
    void createTwoDifferentCityLocation_shouldCreateNewLocation() throws Exception
    {
        sendAndCompareLocationResponse(post(LOCATION), status().isCreated());
        var secondLocation = createValidLocationDto();
        secondLocation.setCityUA("Ірпінь");
        secondLocation.setCityEN("Irpin");
        sendDtoAndGetResultAction(post(LOCATION), secondLocation, status().isCreated());
    }
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = { "location/createTwoLocationWithDifferentDistrict.yml" }, orderBy = "district_en", ignoreCols = "id")
    void createTwoDifferentDistrictLocation_shouldCreateNewLocation() throws Exception
    {
        sendAndCompareLocationResponse(post(LOCATION), status().isCreated());
        var secondLocation = createValidLocationDto();
        secondLocation.setDistrictUA("Шевченконвський");
        secondLocation.setDistrictEN("Shevchenkovskiy");
        sendDtoAndGetResultAction(post(LOCATION), secondLocation, status().isCreated());
    }
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateLocation_shouldReturn404WhenLocationIsNotExisted() throws Exception {
        sendDtoAndGetMvcResult(put(LOCATION_ID, UUID.randomUUID()), createValidLocationDto(), status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("location/update.yml")
    void updateLocation_shouldUpdateExistedLocation() throws Exception {
        sendAndCompareLocationResponse(put(LOCATION_ID, UUID.fromString(existedLocationId)), status().isAccepted());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("location/delete.yml")
    void deleteLocations_shouldDeleteExistedLocation() throws Exception {
        sendUriAndGetMvcResult(delete(LOCATION)
                        .param("ids", existedLocationId),
                status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    void createLocationsInitFile_whenDataIsValid_shouldCreateFileAndReturnItsContent() throws Exception {
        MvcResult response = mockMvc.perform(post(LOCATIONS_INIT)
                .content(Files.readString(Path.of(pathToFileParseLocationsFrom), StandardCharsets.UTF_8))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final var responseContent = response.getResponse().getContentAsString();
        int parsedLocationsQuantity = (responseContent.length() - responseContent.replaceAll("(UUID_TO_BIN)", "").length()) / "UUID_TO_BIN".length() ;
        assertEquals(901, parsedLocationsQuantity, "Comparing unique locations number with result");
        assertTrue(Files.size(Path.of(pathToCreateLocationsInitFile)) > 0);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DataSet("database_init.yml")
    void createLocationsInitFile_whenDataIsNotValid_shouldReturnBadRequestAndProperMessage() throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(post(LOCATIONS_INIT),
                "NOT VALID DATA",
                status().isBadRequest());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("JSON parse error: Cannot construct"));
    }
}
