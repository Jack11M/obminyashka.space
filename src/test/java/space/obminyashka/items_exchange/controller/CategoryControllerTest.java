package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.CategoryDto;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.CATEGORY;
import static space.obminyashka.items_exchange.api.ApiKey.CATEGORY_ID;
import static space.obminyashka.items_exchange.util.CategoryTestUtil.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest extends BasicControllerTest {

    @Autowired
    public CategoryControllerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @ParameterizedTest
    @MethodSource("categoryTestData")
    @WithMockUser(username = USERNAME_ADMIN, roles = {ROLE_ADMIN})
    void createCategory_whenCategoryInvalid_shouldReturnBadRequest(MockHttpServletRequestBuilder method, CategoryDto dto, Class<Exception> expectedException) throws Exception {
        final var result = sendDtoAndGetMvcResult(method, dto, status().isBadRequest());

        assertThat(result.getResolvedException(), is(instanceOf(expectedException)));
        assertTrue(result.getResponse().getContentAsString().contains("\"error\":"));
    }

    private static Stream<Arguments> categoryTestData() {
        return Stream.of(
                Arguments.of(post(CATEGORY), createNonExistCategoryDtoWithInvalidSubcategory(), MethodArgumentNotValidException.class),
                Arguments.of(post(CATEGORY), createNonExistCategoryDtoWithInvalidName(), MethodArgumentNotValidException.class),
                Arguments.of(put(CATEGORY_ID, 1L), getUpdatedCategoryDtoWithInvalidName(), MethodArgumentNotValidException.class)
        );
    }
}
