package space.obminyashka.items_exchange;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;


@RequiredArgsConstructor
public abstract class BasicControllerTest {

    protected static final String API = "/api/v1";
    // Advertisement API
    protected static final String ADV = API + "/adv";
    protected static final String ADV_SEARCH_PAGINATED = ADV + "/search/{keyword}?page={page}&size={size}";
    protected static final String ADV_DEFAULT_IMAGE = ADV + "/default-image/{advertisementId}/{imageId}";
    protected static final String ADV_ID = ADV + "/{advertisement_id}";
    protected static final String ADV_FILTER = ADV + "/filter";
    protected static final String ADV_THUMBNAIL = ADV + "/thumbnail";
    protected static final String ADV_THUMBNAIL_PARAMS = ADV_THUMBNAIL + "?page={page}&size={size}";
    // Authorization API
    protected static final String AUTH = API + "/auth";
    protected static final String AUTH_REGISTER = AUTH + "/register";
    protected static final String AUTH_LOGIN = AUTH + "/login";
    protected static final String AUTH_LOGOUT = AUTH + "/logout";
    protected static final String AUTH_REFRESH_TOKEN = AUTH + "/refresh/token";
    // Category API
    protected static final String CATEGORY = API + "/category";
    protected static final String CATEGORY_NAMES = CATEGORY + "/names";
    protected static final String CATEGORY_ALL = CATEGORY + "/all";
    protected static final String CATEGORY_ID = CATEGORY + "/{category_id}";
    protected static final String CATEGORY_SIZES = CATEGORY_ID + "/sizes";
    // Image API
    protected static final String IMAGE = API + "/image";
    protected static final String IMAGE_BY_ADV_ID = IMAGE + "/{advertisement_id}";
    protected static final String IMAGE_RESOURCE = IMAGE_BY_ADV_ID + "/resource";
    protected static final String IMAGE_COUNT = IMAGE_BY_ADV_ID + "/total";
    // Location API
    protected static final String LOCATION = API + "/location";
    protected static final String LOCATION_ID = LOCATION + "/{location_id}";
    protected static final String LOCATION_ALL = LOCATION + "/all";
    protected static final String LOCATIONS_INIT = LOCATION + "/locations-init";
    // Subcategory API
    protected static final String SUBCATEGORY = API + "/subcategory";
    protected static final String SUBCATEGORY_ID = SUBCATEGORY + "/{subcategory_id}";
    protected static final String SUBCATEGORY_NAMES = SUBCATEGORY + "/{category_id}/names";
    // User API
    protected static final String USER = API + "/user";
    protected static final String USER_MY_INFO = USER + "/my-info";
    protected static final String USER_MY_ADV = USER + "/my-adv";
    protected static final String USER_CHILD = USER + "/child";
    protected static final String USER_SERVICE = USER + "/service";
    protected static final String USER_SERVICE_CHANGE_PASSWORD = USER_SERVICE + "/pass";
    protected static final String USER_SERVICE_CHANGE_EMAIL = USER_SERVICE + "/email";
    protected static final String USER_SERVICE_CHANGE_AVATAR = USER_SERVICE + "/avatar";
    protected static final String USER_SERVICE_DELETE = USER_SERVICE + "/delete";
    protected static final String USER_SERVICE_RESTORE = USER_SERVICE + "/restore";

    protected final MockMvc mockMvc;

    protected <T> MvcResult sendDtoAndGetMvcResult(MockHttpServletRequestBuilder method, T dto, ResultMatcher expectedStatus) throws Exception {
        return this.sendDtoAndGetResultAction(method, dto, expectedStatus).andReturn();
    }

    protected <T> ResultActions sendDtoAndGetResultAction(MockHttpServletRequestBuilder method, T dto, ResultMatcher expectedStatus) throws Exception {
        return getResultActionsAndExpectStatus(method, dto, expectedStatus);
    }

    protected <T> ResultActions sendDtoWithHeadersAndGetResultAction(MockHttpServletRequestBuilder method, T dto,
                                                                     ResultMatcher expectedStatus,
                                                                     HttpHeaders headers) throws Exception {
        return getResultActionsAndExpectStatus(method.headers(headers), dto, expectedStatus);
    }

    protected MvcResult sendUriAndGetMvcResult(MockHttpServletRequestBuilder method, ResultMatcher expectedStatus) throws Exception {
        return this.sendUriAndGetResultAction(method, expectedStatus).andReturn();
    }

    protected ResultActions sendUriAndGetResultAction(MockHttpServletRequestBuilder method, ResultMatcher expectedStatus) throws Exception {
        return getResultActionsAndExpectStatus(expectedStatus, method);
    }

    protected MvcResult sendUriWithHeadersAndGetMvcResult(MockHttpServletRequestBuilder method,
                                                          ResultMatcher expectedStatus,
                                                          HttpHeaders headers) throws Exception {
        return this.sendUriWithHeadersAndGetResultAction(method, expectedStatus, headers).andReturn();
    }

    protected ResultActions sendUriWithHeadersAndGetResultAction(MockHttpServletRequestBuilder method,
                                                                 ResultMatcher expectedStatus,
                                                                 HttpHeaders headers) throws Exception {
        return getResultActionsAndExpectStatus(expectedStatus, method.headers(headers));
    }

    private ResultActions getResultActionsAndExpectStatus(ResultMatcher expectedStatus,
                                                          MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder)
                .andDo(print())
                .andExpect(expectedStatus);
    }

    protected void verifyResultException(MvcResult mvcResult, Class<? extends Exception> exceptionClass, String exceptionMessage) {
        final var resolvedException = mvcResult.getResolvedException();
        assertNotNull(resolvedException);
        assertThat(resolvedException, is(instanceOf(exceptionClass)));
        assertEquals(exceptionMessage, resolvedException.getLocalizedMessage());
    }

    private <T> ResultActions getResultActionsAndExpectStatus(MockHttpServletRequestBuilder builder, T dto,
                                                              ResultMatcher expectedStatus) throws Exception {
        return mockMvc.perform(builder
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(expectedStatus);
    }
}
