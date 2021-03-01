package com.hillel.items_exchange.end2end;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.AgeRange;
import com.hillel.items_exchange.model.enums.Gender;
import com.hillel.items_exchange.model.enums.Season;
import com.hillel.items_exchange.service.UserService;
import com.hillel.items_exchange.util.AdvertisementDtoCreatingUtil;
import com.hillel.items_exchange.util.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DBRider
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class AdvertisementFlowTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private AdvertisementDto nonExistDto;
    private AdvertisementDto existDtoForUpdate;
    private long validId;
    private long validId6;

    @BeforeEach
    void setUp() {
        nonExistDto = AdvertisementDtoCreatingUtil.createNonExistAdvertisementDto();
        validId = 1L;
        validId6 = 6L;
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void findPaginated_shouldReturnSelectedQuantity() throws Exception {
        int page = 0;
        int size = 2;
        MvcResult mvcResult = mockMvc.perform(get("/adv?page={page}&size={size}", page, size)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        AdvertisementDto[] advertisementsDtos = JsonConverter.jsonToObject(json, AdvertisementDto[].class);
        assertEquals(size, advertisementsDtos.length);
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAllAdvertisements_shouldReturnAdvertisementsByTopic() throws Exception {
        mockMvc.perform(get("/adv/topic/{topic}", "ses")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].topic").value("Blouses"))
                .andExpect(jsonPath("$[1].topic").value("Dresses"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementIfExists() throws Exception {
        mockMvc.perform(get("/adv/{advertisement_id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.advertisementId").value(1))
                .andExpect(jsonPath("$.topic").value("topic"))
                .andExpect(jsonPath("$.ownerName").value("super admin"))
                .andExpect(jsonPath("$.age").value("14+"))
                .andExpect(jsonPath("$.createdDate").value("01.01.2019"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    void getAdvertisement_shouldReturnAdvertisementsIfAnyValueExists() throws Exception {
        AdvertisementFilterDto dto = AdvertisementFilterDto.builder()
                .season(Season.SUMMER)
                .gender(Gender.FEMALE)
                .age(AgeRange.FROM_10_TO_12)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/adv/filter")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdvertisementDto[] advertisementDtos = JsonConverter.jsonToObject(contentAsString, AdvertisementDto[].class);
        assertEquals(1, advertisementDtos.length);
        assertEquals(4, advertisementDtos[0].getId());
        assertEquals(AgeRange.FROM_10_TO_12, advertisementDtos[0].getAge());
        assertEquals(Gender.FEMALE, advertisementDtos[0].getGender());
        assertEquals(Season.SUMMER, advertisementDtos[0].getSeason());
    }

    @Test
    @WithMockUser(username = "deletedUser")
    @Transactional
    @DataSet({"database_init.yml", "user/deleted_user_init.yml"})
    void getFirst10BySearchParameters_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        AdvertisementFilterDto dto = AdvertisementFilterDto.builder()
                .season(Season.SUMMER)
                .gender(Gender.FEMALE)
                .age(AgeRange.FROM_10_TO_12)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/adv/filter")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        User deletedUser = userService.findByUsernameOrEmail("deletedUser@gmail.com").orElseThrow();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains(getIllegalOperationMessage(deletedUser)));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/create.yml", ignoreCols = {"created", "updated"})
    void createAdvertisement_shouldCreateValidAdvertisement() throws Exception {
        mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "deletedUser")
    @Transactional
    @DataSet({"database_init.yml", "user/deleted_user_init.yml"})
    void createAdvertisement_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/adv")
                .content(asJsonString(nonExistDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        User deletedUser = userService.findByUsernameOrEmail("deletedUser@gmail.com").orElseThrow();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains(getIllegalOperationMessage(deletedUser)));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/update.yml", ignoreCols = "updated")
    void updateAdvertisement_shouldUpdateExistedAdvertisementWithNewLocation() throws Exception {
        existDtoForUpdate = AdvertisementDtoCreatingUtil
                .createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory();

        mockMvc.perform(put("/adv")
                .content(asJsonString(existDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.description").value("new description"))
                .andExpect(jsonPath("$.topic").value("new topic"))
                .andExpect(jsonPath("$.wishesToExchange").value("BMW"))
                .andExpect(jsonPath("$.location.city").value("Odessa"))
                .andExpect(jsonPath("$.location.district").value("Odessa district"));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/updateAdvLocation.yml", ignoreCols = "updated")
    void updateAdvertisement_shouldUpdateExistedAdvertisementWithUpdatedLocation() throws Exception {
        existDtoForUpdate = AdvertisementDtoCreatingUtil
                .createExistAdvertisementDtoForUpdateWithUpdatedLocationChangedImagesAndSubcategory();

        mockMvc.perform(put("/adv")
                .content(asJsonString(existDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.description").value("new description"))
                .andExpect(jsonPath("$.topic").value("new topic"))
                .andExpect(jsonPath("$.wishesToExchange").value("BMW"))
                .andExpect(jsonPath("$.location.city").value("New Vasyuki"))
                .andExpect(jsonPath("$.location.district").value("New Vasyuki district"));
    }

    @Test
    @WithMockUser(username = "deletedUser")
    @Transactional
    @DataSet({"database_init.yml", "user/deleted_user_init.yml"})
    void updateAdvertisement_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        existDtoForUpdate = AdvertisementDtoCreatingUtil
                .createExistAdvertisementDtoForUpdateWithNewLocationChangedImagesAndSubcategory();

        MvcResult mvcResult = mockMvc.perform(put("/adv")
                .content(asJsonString(existDtoForUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        User deletedUser = userService.findByUsernameOrEmail("deletedUser@gmail.com").orElseThrow();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains(getIllegalOperationMessage(deletedUser)));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/delete.yml")
    void deleteAdvertisement_shouldDeleteExistedAdvertisement() throws Exception {
        mockMvc.perform(delete("/adv/{advertisement_id}", validId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "deletedUser")
    @Transactional
    @DataSet({"database_init.yml", "user/deleted_user_init.yml"})
    void deleteAdvertisement_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        AdvertisementDto existDto = AdvertisementDtoCreatingUtil.createExistAdvertisementDto();

        MvcResult mvcResult = mockMvc.perform(delete("/adv/{advertisementId}", validId6)
                .content(asJsonString(existDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        User deletedUser = userService.findByUsernameOrEmail("deletedUser@gmail.com").orElseThrow();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains(getIllegalOperationMessage(deletedUser)));
    }

    @Test
    @WithMockUser(username = "admin")
    @Transactional
    @DataSet("database_init.yml")
    @ExpectedDataSet(value = "advertisement/setDefaultImage.yml", ignoreCols = {"created", "updated"})
    void setDefaultImage_success() throws Exception {
        mockMvc.perform(post("/adv/default-image/{advertisementId}/{imageId}", validId, validId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "deletedUser")
    @Transactional
    @DataSet({"database_init.yml", "user/deleted_user_init.yml"})
    void setDefaultImage_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/adv/default-image/{advertisementId}/{imageId}",
                validId6, validId))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        User deletedUser = userService.findByUsernameOrEmail("deletedUser@gmail.com").orElseThrow();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains(getIllegalOperationMessage(deletedUser)));
    }

    private String getIllegalOperationMessage(User deletedUser) {
        return getMessageSource("exception.illegal.operation")
                .concat(". ")
                .concat(getParametrizedMessageSource("account.deleted.first",
                        userService.getDaysBeforeDeletion(deletedUser)));
    }
}
