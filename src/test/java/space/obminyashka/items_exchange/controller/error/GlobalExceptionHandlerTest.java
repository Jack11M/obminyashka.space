package space.obminyashka.items_exchange.controller.error;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.exception.IllegalIdentifierException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.util.AdvertisementDtoCreatingUtil;
import space.obminyashka.items_exchange.util.CategoryTestUtil;
import space.obminyashka.items_exchange.util.ChildDtoCreatingUtil;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest extends BasicControllerTest {

    @Autowired
    private WebApplicationContext context;
    private AdvertisementModificationDto nonExistDto;
    private AdvertisementModificationDto existDto;
    private List<ChildDto> childDtoList;

    @Autowired
    public GlobalExceptionHandlerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @BeforeEach
    void setup() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementModificationDto();
        existDto = AdvertisementDtoCreatingUtil.createExistAdvertisementModificationDto();
        childDtoList = ChildDtoCreatingUtil.generateTestChildren(10);
    }

    @Test
    @WithMockUser(username = "anonymous")
    @DataSet("database_init.yml")
    void testHandleUserNotFoundException() throws Exception {
        MvcResult result = sendDtoAndGetMvcResult(multipart(ADV).file(new MockMultipartFile("image", new byte[0])), nonExistDto, status().isNotFound());
        assertThat(result.getResolvedException(), is(instanceOf(UsernameNotFoundException.class)));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    void testHandleSecurityException() throws Exception {
        MvcResult result = getResult(HttpMethod.PUT, "/api/v1/adv", existDto, status().isForbidden());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalOperationException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void testHandleElementsNumberExceedException() throws Exception {

        MvcResult result = getResult(HttpMethod.POST, "/api/v1/user/child", childDtoList, status().isNotAcceptable());
        assertThat(result.getResolvedException(), is(instanceOf(ElementsNumberExceedException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    void testHandleIllegalIdentifierException() throws Exception {
        existDto.setSubcategoryId(0L);
        MvcResult result = getResult(HttpMethod.PUT, "/api/v1/adv", existDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(get("/api/v1/image/{advertisement_id}", -1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(ConstraintViolationException.class)));
    }

    @Test
    void testHandleMethodArgumentNotValidException() throws Exception {
        final CategoryDto newCategoryDtoWithIdNotZero = CategoryTestUtil.createNonExistCategoryDtoWithInvalidId();
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(post("/api/v1/category")
                .content(asJsonString(newCategoryDtoWithIdNotZero))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentNotValidException.class)));
    }

    private MvcResult getResult(HttpMethod httpMethod, String path, Object dto,
                                ResultMatcher matcher) throws Exception {

        MockHttpServletRequestBuilder builder = request(httpMethod, path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto));
        return mockMvc.perform(builder).andExpect(matcher).andReturn();
    }
}
