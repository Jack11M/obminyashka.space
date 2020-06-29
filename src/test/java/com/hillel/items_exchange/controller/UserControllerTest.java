package com.hillel.items_exchange.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.util.ChildDtoCreatingUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void getUserDto_shouldReturnUserDtoIfExists() throws Exception {
        mockMvc.perform(get("/user/info/{id}", 1L)
                .content(asJsonString("admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void negativeTestReceivingInformationAboutAnotherUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/info/{id}", 555555L)
                .content(asJsonString("admin"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        assertThat(result.getResolvedException(), is(instanceOf(AccessDeniedException.class)));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void getChild_Success() throws Exception {
        mockMvc.perform(get("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].sex").value("male"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].sex").value("female"));

    }

    @Test
    @WithMockUser(username = "no-such-user")
    @Transactional
    @DataSet("database_init.yml")
    void getChild_Fail_NoSuchUser() throws Exception {
        mockMvc.perform(get("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "child/create.yml")
    void addChild_Success() throws Exception {
        mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getValidChildDtoForCreate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "child/delete.yml")
    void removeChild_Success() throws Exception {
        mockMvc.perform(delete("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getValidChildDtoForDelete())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "child/update.yml")
    void updateChild_Success() throws Exception {
        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getValidChildDtoForUpdate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void addChild_Fail_EmptyDto() throws Exception {
        mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void removeChild_Fail_EmptyDto() throws Exception {
        mockMvc.perform(delete("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateChild_Fail_EmptyDto() throws Exception {
        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "no-such-user")
    @Transactional
    @DataSet("database_init.yml")
    void addChild_Fail_AbsentUser() throws Exception {
        mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getValidChildDtoForCreate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "no-such-user")
    @Transactional
    @DataSet("database_init.yml")
    void removeChild_Fail_AbsentUser() throws Exception {
        mockMvc.perform(delete("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getValidChildDtoForCreate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "no-such-user")
    @Transactional
    @DataSet("database_init.yml")
    void updateChild_Fail_AbsentUser() throws Exception {
        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getValidChildDtoForCreate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void addChild_Fail_NotValidDto_IdBiggerThanZero() throws Exception {
        mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getNotValidChildDtoForCreate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void removeChild_Fail_NotValidDto_NoSuchChildInUser() throws Exception {
        mockMvc.perform(delete("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getNotValidChildDtoForDelete())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateChild_Fail_NotValidDto_NoSuchChildInUser() throws Exception {
        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getNotValidChildDtoForUpdate())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void removeChild_Fail_NotValidDto_DuplicatedId() throws Exception {
        mockMvc.perform(delete("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getNotValidChildDtoDuplicatedId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void updateChild_Fail_NotValidDto_DuplicatedId() throws Exception {
        mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ChildDtoCreatingUtil.getNotValidChildDtoDuplicatedId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}