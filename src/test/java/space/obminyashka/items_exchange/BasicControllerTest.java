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
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;


@RequiredArgsConstructor
public abstract class BasicControllerTest {

    protected final MockMvc mockMvc;

    protected <T> MvcResult sendDtoAndGetMvcResult(MockHttpServletRequestBuilder method, T dto, ResultMatcher expectedStatus) throws Exception {
        return this.sendDtoAndGetResultAction(method, dto, expectedStatus).andReturn();
    }

    protected <T> ResultActions sendDtoAndGetResultAction(MockHttpServletRequestBuilder method, T dto, ResultMatcher expectedStatus) throws Exception {
        return getResultActionsAndExpectStatus(method, dto, expectedStatus);
    }

    protected <T> MvcResult sendRequestAndGetMvcResult(MockHttpServletRequestBuilder method, T request, ResultMatcher expectedStatus) throws Exception {
        return this.sendRequestAndGetResultAction(method, request, expectedStatus).andReturn();
    }

    protected <T> ResultActions sendRequestAndGetResultAction(MockHttpServletRequestBuilder method, T request, ResultMatcher expectedStatus) throws Exception {
        return getResultActionsAndExpectStatus(method, request, expectedStatus);
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
                .andExpect(expectedStatus);
    }
}
