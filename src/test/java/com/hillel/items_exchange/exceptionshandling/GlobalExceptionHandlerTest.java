package com.hillel.items_exchange.exceptionshandling;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.*;
import com.hillel.items_exchange.util.AdvertisementDtoCreatingUtil;
import com.hillel.items_exchange.util.CategoryTestUtil;
import com.hillel.items_exchange.util.ChildDtoCreatingUtil;
import com.hillel.items_exchange.util.UserDtoCreatingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    private AdvertisementDto nonExistDto;
    private AdvertisementDto existDto;
    private UserDto userDtoWithChangedUsername;
    private List<ChildDto> childDtoList;

    @BeforeEach
    void setup() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementDto();
        existDto = AdvertisementDtoCreatingUtil.createExistAdvertisementDto();
        userDtoWithChangedUsername = UserDtoCreatingUtil.createUserDtoForUpdatingWithChangedUsernameWithoutPhones();
        childDtoList = ChildDtoCreatingUtil.getChildrenDtoList(10);
    }

    @Test
    @WithMockUser(username = "anonymous")
    void testHandleUserNotFoundException() throws Exception {
        MvcResult result = getResult(HttpMethod.POST, "/adv", nonExistDto, status().isNotFound());
        assertThat(result.getResolvedException(), is(instanceOf(UsernameNotFoundException.class)));
    }

    @Test
    @WithMockUser(username = "user")
    @DataSet("database_init.yml")
    void testHandleSecurityException() throws Exception {
        MvcResult result = getResult(HttpMethod.PUT, "/adv", existDto, status().isConflict());
        assertThat(result.getResolvedException(), is(instanceOf(DataConflictException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void testHandleElementsNumberExceedException() throws Exception {

        MvcResult result = getResult(HttpMethod.POST, "/user/child", childDtoList, status().isNotAcceptable());
        assertThat(result.getResolvedException(), is(instanceOf(ElementsNumberExceedException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    void testHandleIllegalIdentifierException() throws Exception {
        existDto.setSubcategoryId(0L);
        MvcResult result = getResult(HttpMethod.PUT, "/adv", existDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    @DataSet("database_init.yml")
    void testHandleInvalidDtoException() throws Exception {
        userDtoWithChangedUsername.setUsername("admin");
        userDtoWithChangedUsername.setEmail("user@gmail.com");
        MvcResult result = getResult(HttpMethod.PUT, "/user/info", userDtoWithChangedUsername, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(InvalidDtoException.class)));
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(get("/image/{advertisement_id}", -1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(ConstraintViolationException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    void testHandleIllegalOperationException() throws Exception {
        MvcResult result = getResult(HttpMethod.PUT, "/user/info", userDtoWithChangedUsername, status().isForbidden());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalOperationException.class)));
    }

    @Test
    void testHandleMethodArgumentNotValidException() throws Exception {
        final CategoryDto newCategoryDtoWithIdNotZero = CategoryTestUtil.createNonExistCategoryDtoWithInvalidId();
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(post("/category")
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