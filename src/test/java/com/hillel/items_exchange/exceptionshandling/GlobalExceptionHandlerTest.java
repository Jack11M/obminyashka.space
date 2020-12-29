package com.hillel.items_exchange.exceptionshandling;

import com.hillel.items_exchange.controller.AdvertisementController;
import com.hillel.items_exchange.controller.CategoryController;
import com.hillel.items_exchange.controller.UserController;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.exception.handler.GlobalExceptionHandler;
import com.hillel.items_exchange.util.AdvertisementDtoCreatingUtil;
import com.hillel.items_exchange.util.CategoryTestUtil;
import com.hillel.items_exchange.util.ChildDtoCreatingUtil;
import com.hillel.items_exchange.util.UserDtoCreatingUtil;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private AdvertisementDto nonExistDto;
    private AdvertisementDto existDto;
    private UserDto userDtoWithChangedUsername;
    private List<ChildDto> childDtoList;

    @Mock
    AdvertisementController advertisementController;

    @Mock
    UserController userController;

    @Mock
    CategoryController categoryController;

    @BeforeEach
    void setup() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementDto();
        existDto = AdvertisementDtoCreatingUtil.createExistAdvertisementDto();
        userDtoWithChangedUsername = UserDtoCreatingUtil.createUserDtoForUpdatingWithChangedUsernameWithoutPhones();
        childDtoList = ChildDtoCreatingUtil.getChildrenDtoList(1);

        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController, categoryController, userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testHandleUserNotFoundException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(UsernameNotFoundException.class);
        MvcResult result = getResult(HttpMethod.POST, "/adv", nonExistDto, status().isNotFound());
        assertThat(result.getResolvedException(), is(instanceOf(UsernameNotFoundException.class)));
    }

    @Test
    void testHandleSecurityException() throws Exception {
        when(advertisementController.updateAdvertisement(any(), any())).thenThrow(IllegalOperationException.class);
        MvcResult result = getResult(HttpMethod.PUT, "/adv", nonExistDto, status().isForbidden());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalOperationException.class)));
    }

    @Test
    public void testHandleEntityNotFoundException() throws Exception {
        when(categoryController.getCategoryById(anyLong())).thenThrow(EntityNotFoundException.class);
        MvcResult result = getResult(HttpMethod.GET, "/category/{category_id}", -1L, status().isNotFound());
        assertThat(result.getResolvedException(), is(instanceOf(EntityNotFoundException.class)));
    }

    @Test
    public void testHandleIllegalIdentifierException() throws Exception {
        when(advertisementController.updateAdvertisement(any(), any())).thenThrow(IllegalIdentifierException.class);
        MvcResult result = getResult(HttpMethod.PUT, "/adv", nonExistDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    void testHandleInvalidDtoException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(IllegalIdentifierException.class);
        MvcResult result = getResult(HttpMethod.POST, "/adv", nonExistDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalIdentifierException.class)));
    }

    @Test
    public void testHandleSqlException() throws Exception {
        when(advertisementController.updateAdvertisement(any(), any())).thenThrow(DataIntegrityViolationException.class);
        MvcResult result = getResult(HttpMethod.PUT, "/adv", existDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(DataIntegrityViolationException.class)));
    }

    @Test
    public void testHandleIllegalArgumentException() throws Exception {
        when(advertisementController.createAdvertisement(any(), any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = getResult(HttpMethod.POST, "/adv", nonExistDto, status().isBadRequest());
        assertThat(result.getResolvedException(), is(instanceOf(IllegalArgumentException.class)));
    }

    @Test
    public void testHandleConstraintViolationException() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MvcResult result = mockMvc.perform(get("/image/{product_id}", -1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(ConstraintViolationException.class)));
    }

    @Test
    public void testHandleIllegalOperationException() throws Exception {
        when(userController.updateUserInfo(any(), any())).thenThrow(IllegalOperationException.class);
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

    private MvcResult getResult(HttpMethod httpMethod, String path, long uriVars,
                                ResultMatcher matcher) throws Exception {

        MockHttpServletRequestBuilder builder = request(httpMethod, path, uriVars)
                .accept(MediaType.APPLICATION_JSON);
        return mockMvc.perform(builder).andExpect(matcher).andReturn();
    }
}
