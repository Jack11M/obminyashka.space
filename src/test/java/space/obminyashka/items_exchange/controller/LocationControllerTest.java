package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.LocationDto;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.LOCATION;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.createLocationDto;
import static space.obminyashka.items_exchange.util.LocationDtoCreatingUtil.createLocationDtoForCreatingWithInvalidCity;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest extends BasicControllerTest {

    @Autowired
    public LocationControllerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @WithMockUser(username = "admin")
    @ParameterizedTest
    @MethodSource("locationPostTestData")
    void createLocation_shouldNotReturnOkWhenNotValid(LocationDto dto, ResultMatcher expectedStatus) throws Exception {
        sendDtoAndGetMvcResult(post(LOCATION), dto, expectedStatus);
    }

    private static Stream<Arguments> locationPostTestData() {
        return Stream.of(
                Arguments.of(createLocationDto(), status().isForbidden()),
                Arguments.of(createLocationDtoForCreatingWithInvalidCity(), status().isBadRequest())
        );
    }

    @WithMockUser(username = "admin")
    @Test
    void deleteLocations_shouldReturn409WhenUserHasNotRoleAdmin() throws Exception {
        sendUriAndGetMvcResult(delete(LOCATION).param("ids", "2c5467f3-b7ee-48b1-9451-7028255b757b"), status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteLocations_shouldReturn400WhenLocationIdIsNotExisted() throws Exception {
        sendUriAndGetMvcResult(delete(LOCATION).param("ids", "1", "ids", "555"), status().isBadRequest());
    }
}
