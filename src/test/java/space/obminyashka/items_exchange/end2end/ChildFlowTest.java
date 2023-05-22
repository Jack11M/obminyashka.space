package space.obminyashka.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import space.obminyashka.items_exchange.BasicControllerTest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.USER_CHILD;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getTestChildren;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ChildFlowTest extends BasicControllerTest {

    @Autowired
    public ChildFlowTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    @WithMockUser("admin")
    @DataSet("database_init.yml")
    void getChildren_success_shouldReturnUsersChildren() throws Exception {
        sendUriAndGetResultAction(get(USER_CHILD), status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sex").value("MALE"))
                .andExpect(jsonPath("$[1].sex").value("FEMALE"));
    }

    @Test
    @WithMockUser("admin")
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "children/update.yml", orderBy = {"created", "name", "birth_date"}, ignoreCols = "id")
    void updateChildren_whenRequestValid_shouldReturnHttpStatusOk() throws Exception {
        var validUpdatingChildDtoJson = getTestChildren(2018);

        sendDtoAndGetResultAction(put(USER_CHILD), validUpdatingChildDtoJson, status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
