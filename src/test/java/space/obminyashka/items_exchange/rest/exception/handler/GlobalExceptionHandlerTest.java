package space.obminyashka.items_exchange.rest.exception.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.exception.bad_request.IllegalIdentifierException;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;
import space.obminyashka.items_exchange.util.data_producer.AdvertisementModificationDtoProducer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GlobalExceptionHandlerTest extends BasicControllerTest {
    private AdvertisementModificationDto nonExistDto;
    private AdvertisementModificationDto existDto;

    @Autowired
    public GlobalExceptionHandlerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @BeforeEach
    void setup() {
        nonExistDto = AdvertisementModificationDtoProducer.createNonExistAdvertisementModificationDto();
        existDto = AdvertisementModificationDtoProducer.createExistAdvertisementModificationDto();
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DataSet("database_init.yml")
    void testHandleUserNotFoundException() throws Exception {
        final var dtoJson = new MockMultipartFile("dto", "json", MediaType.APPLICATION_JSON_VALUE, asJsonString(nonExistDto).getBytes());
        MvcResult result = sendUriAndGetMvcResult(multipart(ApiKey.ADV)
                .file(new MockMultipartFile("image", new byte[0]))
                .file(dtoJson), status().isNotFound());
        assertThat(result.getResolvedException(), is(instanceOf(UsernameNotFoundException.class)));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    void testHandleSecurityException() throws Exception {
        MvcResult result = sendDtoAndGetMvcResult(put(ApiKey.ADV_ID, existDto.getId()), existDto, status().isForbidden());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalOperationException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    void testHandleIllegalIdentifierException() throws Exception {
        existDto.setSubcategoryId(0L);
        MvcResult result = sendDtoAndGetMvcResult(put(ApiKey.ADV_ID, existDto.getId()), existDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        MvcResult result = sendUriAndGetMvcResult(get(ApiKey.ADV_ID, 2.8), status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentTypeMismatchException.class)));
    }
}
