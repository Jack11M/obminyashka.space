package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import space.obminyashka.items_exchange.BasicControllerTest;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationControllerTest extends BasicControllerTest {

    @Autowired
    public AuthorizationControllerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @ParameterizedTest
    @MethodSource("invalidTestData")
    void register_whenUserRegistrationDtoIsEmpty_shouldReturnBadRequest(Object uriVar) throws Exception {
        sendUriAndGetMvcResult(post(AUTH_REGISTER, uriVar), status().isBadRequest());
    }

    private static Stream<Arguments> invalidTestData() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null)
        );
    }
}
