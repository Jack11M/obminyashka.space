package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.dto.LocationDto;
import space.obminyashka.items_exchange.rest.exception.DataConflictException;
import space.obminyashka.items_exchange.rest.exception.not_found.EntityIdNotFoundException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.LOCATION;
import static space.obminyashka.items_exchange.rest.api.ApiKey.LOCATION_ALL;
import static space.obminyashka.items_exchange.rest.api.ApiKey.LOCATION_AREA;
import static space.obminyashka.items_exchange.rest.api.ApiKey.LOCATION_CITY;
import static space.obminyashka.items_exchange.rest.api.ApiKey.LOCATION_ID;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage.LOCATION_ALREADY_EXIST;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_LOCATION_ID;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.AREA_EN;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.AREA_UA;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.CITY_EN;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.CITY_UA;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.DISTRICT_EN;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.DISTRICT_UA;
import static space.obminyashka.items_exchange.util.data_producer.LocationDtoProducer.createValidLocationDto;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LocationFlowTest extends BasicControllerTest {
    private static final String existedLocationId = "2c5467f3-b7ee-48b1-9451-7028255b757b";
    private static final String existedAreaId = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10";
    private static final String invalidLocationId = "61731cc8-8104-49f0-b2c3-5a52e576ab28";

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
    @DataSet("location/location_init.yml")
    void getAllAreas_shouldReturnAllAreas() throws Exception {
        sendUriAndGetResultAction(get(LOCATION_AREA), status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].nameEn").value("Odeska"))
                .andExpect(jsonPath("$[0].nameUa").value("Одеська"));
    }

    @Test
    @DataSet("location/location_init.yml")
    void getAllCityByAreaId_shouldReturnAllCity() throws Exception {
        sendUriAndGetResultAction(get(LOCATION_CITY).param("areaId", existedAreaId), status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nameEn").value("Odesa"))
                .andExpect(jsonPath("$[0].nameUa").value("Одеса"));
    }

    @Test
    @DataSet("location/location_init.yml")
    void CityByDistrictId_shouldReturnException_whenCityIsNotFound() throws Exception {
        String wrongAreaId = "f93c84bf-ba42-4577-b7e7-5cda1547c371";
        var resultActions = sendUriAndGetResultAction(get(LOCATION_CITY)
                .param("areaId", wrongAreaId), status().isNotFound());

        Assertions.assertThat(resultActions.andReturn().getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class)
                .hasMessage(getParametrizedMessageSource(INVALID_LOCATION_ID, invalidLocationId));
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

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "location/createSameLocation.yml", orderBy = "city_en", ignoreCols = "id")
    void createLocation_shouldGetConflictResponse_whenCreateSameLocation() throws Exception {
        var sameLocation = LocationDto.builder()
                .cityUA("Харків")
                .districtUA("Харківський район")
                .areaUA("Харківська область")
                .cityEN("Kharkiv")
                .districtEN("Kharkivska district")
                .areaEN("Kharkivska area")
                .build();
        MvcResult mvcResult = sendDtoAndGetResultAction(post(LOCATION), sameLocation, status().isConflict()).andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(DataConflictException.class)
                .hasMessage(getMessageSource(LOCATION_ALREADY_EXIST));

    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = {"location/createTwoLocationWithDifferentArea.yml"}, orderBy = "area_en", ignoreCols = "id")
    void createLocation_shouldCreateNewLocationWithDifferentArea() throws Exception {
        var locationWithDifferentArea = LocationDto.builder()
                .cityUA("Харків")
                .districtUA("Харківський район")
                .areaUA("Київська область")
                .cityEN("Kharkiv")
                .districtEN("Kharkivska district")
                .areaEN("Kyivska area")
                .build();
        sendDtoAndGetResultAction(post(LOCATION), locationWithDifferentArea, status().isCreated());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = {"location/createTwoLocationWithDifferentCity.yml"}, orderBy = "city_en", ignoreCols = "id")
    void createLocation_shouldCreateNewLocationWithDifferentCity() throws Exception {
        var locationWithDifferentCity = LocationDto.builder()
                .cityUA("Дергачі")
                .districtUA("Харківський район")
                .areaUA("Харківська область")
                .cityEN("Dergachi")
                .districtEN("Kharkivska district")
                .areaEN("Kharkivska area")
                .build();
        sendDtoAndGetResultAction(post(LOCATION), locationWithDifferentCity, status().isCreated());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = {"location/createTwoLocationWithDifferentDistrict.yml"}, orderBy = "district_en", ignoreCols = "id")
    void createLocation_shouldCreateNewLocationWithDifferentDistrict() throws Exception {
        var locationWithDifferentDistrict = LocationDto.builder()
                .cityUA("Харків")
                .districtUA("Шевченконвський район")
                .areaUA("Харківська область")
                .cityEN("Kharkiv")
                .districtEN("Shevchenkovskiy district")
                .areaEN("Kharkivska area")
                .build();
        sendDtoAndGetResultAction(post(LOCATION), locationWithDifferentDistrict, status().isCreated());
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
}
