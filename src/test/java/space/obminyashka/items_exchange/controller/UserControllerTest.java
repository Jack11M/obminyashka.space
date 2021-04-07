package space.obminyashka.items_exchange.controller;

import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.annotation.FieldMatch;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getJsonOfChildrenDto;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    public static final String PATH_USER_CHANGE_PASSWORD = "/user/service/pass";
    public static final String PATH_USER_CHANGE_EMAIL = "/user/service/email";
    public static final String PATH_USER_CHANGE_AVATAR = "/user/service/avatar";
    public static final String USER_SERVICE_DELETE = "/user/service/delete";
    public static final String USER_SERVICE_RESTORE = "/user/service/restore";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @Value("${max.children.amount}")
    private int maxChildrenAmount;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    void negativeTestReceivingInformationAboutAnotherUser() throws Exception {
        mockMvc.perform(get("/user/my-info")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn403WhenUsernameIsChanged() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.update(any(), any())).thenThrow(new IllegalOperationException(
                getMessageSource("exception.illegal.field.change") + user.getUsername()));

        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedUsernameWithoutPhones(), status().isForbidden())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(
                getMessageSource("exception.illegal.field.change") + user.getUsername()));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn403WhenLastOnlineTimeIsChanged() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.update(any(), any())).thenThrow(new IllegalOperationException(
                getMessageSource("exception.illegal.field.change") + user.getLastOnlineTime()));

        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedLastOnlineTimeWithoutPhones(), status().isForbidden())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(
                getMessageSource("exception.illegal.field.change") + user.getLastOnlineTime()));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn403WhenChildrenAreChanged() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.update(any(), any())).thenThrow(new IllegalOperationException(
                getMessageSource("exception.illegal.field.change") + user.getChildren()));

        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedChildrenWithoutPhones(), status().isForbidden())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(
                getMessageSource("exception.illegal.field.change") + user.getChildren()));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn403WhenPhonesAreChanged() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.update(any(), any())).thenThrow(new IllegalOperationException(
                getMessageSource("exception.illegal.field.change") + user.getPhones()));

        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithPhones(), status().isForbidden())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(
                getMessageSource("exception.illegal.field.change") + user.getPhones()));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn400WhenShortFirstName() throws Exception {
        getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithInvalidShortFNameWithoutPhones(), status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn400WhenFirstNameContainsTwoWords() throws Exception {
        getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithInvalidFNameWithoutPhones(), status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_shouldReturn400WhenDuplicateEmail() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.existsByEmail(any())).thenReturn(true);

        MvcResult mvcResult = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithDuplicateEmailWithoutPhones(), status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("email.duplicate")));
    }

    @Test
    @WithMockUser(username = "deletedUser")
    void updateUserInfo_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        user.setStatus(Status.DELETED);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.getDaysBeforeDeletion(any())).thenReturn(7L);

        MvcResult mvcResult = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChangedEmailAndFNameApAndLNameMinusWithoutPhones(),
                status().isForbidden())
                .andDo(print())
                .andReturn();

        User deletedUser = userService.findByUsernameOrEmail("deletedUser@gmail.com").orElseThrow();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        assertTrue(contentAsString.contains(getMessageSource("exception.illegal.operation")
                .concat(". ")
                .concat(getParametrizedMessageSource("account.self.delete.request",
                        userService.getDaysBeforeDeletion(deletedUser)))));
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_NotValidDto_ShouldThrowIllegalIdentifierException() throws Exception {
        String notValidCreatingChildDtoJson = getJsonOfChildrenDto(111L, 222L, 2019);

        final MvcResult mvcResult = mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notValidCreatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString()
                .contains(getMessageSource("invalid.new.entity.id")));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateChild_NotValidDto_ShouldThrowIllegalIdentifierException() throws Exception {
        String notValidUpdatingChildDtoJson = getJsonOfChildrenDto(1L, 999L, 2018);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = mockMvc.perform(put("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notValidUpdatingChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Not all children from dto present in"));
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_BadAmount_ShouldThrowConstraintViolationException() throws Exception {
        String badAmountChildDtoJson = getJsonOfChildrenDto(maxChildrenAmount + 1);

        final MvcResult mvcResult = mockMvc.perform(post("/user/child")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badAmountChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage()
                .contains(getMessageSource("exception.invalid.dto")));
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_BadTotalAmount_ShouldThrowEntityAmountException() throws Exception {
        String badTotalAmountChildDtoJson = getJsonOfChildrenDto(maxChildrenAmount - 1);
        user.setChildren(List.of(
                new Child(1L, Gender.MALE, LocalDate.of(2019, 1, 1), user),
                new Child(2L, Gender.FEMALE, LocalDate.of(2019, 1, 1), user)));
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = mockMvc.perform(post("/user/child/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badTotalAmountChildDtoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andReturn();
        assertTrue(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage()
                .contains(getParametrizedMessageSource("exception.children-amount",
                        maxChildrenAmount)));
    }

    @Test
    @WithMockUser(username = "test")
    void updateUserInfo_shouldReturn403WhenUpdatedUserContainsNewChildren() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.update(any(), any())).thenThrow(new IllegalOperationException(
                getMessageSource("exception.illegal.children.phones.change")));

        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithChildrenWithoutPhones(), status().isForbidden())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString()
                .contains(getMessageSource("exception.illegal.children.phones.change")));
    }

    @Test
    @WithMockUser(username = "test")
    void updateUserInfo_shouldReturn403WhenUpdatedUserContainsNewPhone() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.update(any(), any())).thenThrow(new IllegalOperationException(
                getMessageSource("exception.illegal.children.phones.change")));

        MvcResult result = getResultActions(HttpMethod.PUT, "/user/info",
                createUserDtoForUpdatingWithPhoneWithoutChildren(), status().isForbidden())
                .andDo(print())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString()
                .contains(getMessageSource("exception.illegal.children.phones.change")));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenOldPasswordWrong_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserChangePasswordDto userChangePasswordDto = createUserChangePasswordDto(WRONG_OLD_PASSWORD,
                NEW_PASSWORD,
                NEW_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_PASSWORD, userChangePasswordDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenPasswordConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        UserChangePasswordDto userChangePasswordDto = createUserChangePasswordDto(CORRECT_OLD_PASSWORD,
                NEW_PASSWORD,
                WRONG_NEW_PASSWORD_CONFIRMATION);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_PASSWORD, userChangePasswordDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenEmailConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        UserChangeEmailDto userChangeEmailDto = createUserChangeEmailDto(NEW_VALID_EMAIL, NEW_INVALID_DUPLICATE_EMAIL);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_EMAIL, userChangeEmailDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("invalid.confirm.email")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenUserEnteredOldEmail_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        UserChangeEmailDto userChangeEmailDto = createUserChangeEmailDto(OLD_USER_VALID_EMAIL, OLD_USER_VALID_EMAIL);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_EMAIL, userChangeEmailDto,
                status().isConflict())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("exception.email.old")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenUserEnteredExistedEmail_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.existsByEmail(any())).thenReturn(true);

        UserChangeEmailDto userChangeEmailDto = createUserChangeEmailDto(OLD_ADMIN_VALID_EMAIL, OLD_ADMIN_VALID_EMAIL);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_EMAIL, userChangeEmailDto,
                status().isConflict())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("email.duplicate")));
    }

    @Test
    @WithMockUser(username = "admin")
    void setUserAvatar_whenReceivedBMPImage_shouldThrowUnsupportedMediaTypeException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        MockMultipartFile bmp = new MockMultipartFile("file", "image-bmp.bmp", "image/bmp",
                "image bmp".getBytes());
        mockMvc.perform(multipart(PATH_USER_CHANGE_AVATAR)
                .file(bmp)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_WhenPasswordWrongAndTheSameConfirmation_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(WRONG_OLD_PASSWORD,
                WRONG_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.DELETE, USER_SERVICE_DELETE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_WhenPasswordCorrectAndConfirmationWrong_ShouldThrowIllegalArgumentException()
            throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(CORRECT_OLD_PASSWORD,
                WRONG_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.DELETE, USER_SERVICE_DELETE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    /**
     * In this case annotation
     * {@link FieldMatch}
     * has to work before condition:
     * if (!userService.isPasswordMatches(user, userDeleteOrRestoreDto.getPassword()))}
     * in
     * {@link UserController#selfDeleteRequest(UserDeleteFlowDto, Principal)}
     */
    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_WhenPasswordWrongAndPasswordConfirmationDoesNotMatch_ShouldThrowIllegalArgumentException()
            throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(WRONG_OLD_PASSWORD,
                CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.DELETE, USER_SERVICE_DELETE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser(username = "deletedUser")
    void makeAccountActiveAgain_WhenPasswordWrongAndTheSameConfirmation_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(WRONG_OLD_PASSWORD,
                WRONG_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, USER_SERVICE_RESTORE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    @Test
    @WithMockUser(username = "deletedUser")
    void makeAccountActiveAgain_WhenPasswordCorrectAndConfirmationWrong_ShouldThrowIllegalArgumentException()
            throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(CORRECT_OLD_PASSWORD,
                WRONG_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, USER_SERVICE_RESTORE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser(username = "deletedUser")
    void makeAccountActiveAgain_WhenPasswordWrongAndPasswordConfirmationDoesNotMatch_ShouldThrowIllegalArgumentException()
            throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(WRONG_OLD_PASSWORD,
                CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, USER_SERVICE_RESTORE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser("admin")
    void makeAccountActiveAgain_WhenUserHasNotStatusDeletedShouldThrowIllegalOperationException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(true);

        UserDeleteFlowDto userDeleteFlowDto = createUserDeleteOrRestoreDto(CORRECT_OLD_PASSWORD,
                CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, USER_SERVICE_RESTORE, userDeleteFlowDto,
                status().isForbidden())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("exception.illegal.operation")));
    }

    private <T> ResultActions getResultActions(HttpMethod httpMethod, String path,
                                               T dto, ResultMatcher matcher) throws Exception {
        MockHttpServletRequestBuilder builder = request(httpMethod, path)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto));
        return mockMvc.perform(builder).andExpect(matcher);
    }

    private User createUser() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail(OLD_USER_VALID_EMAIL);
        user.setStatus(Status.ACTIVE);
        user.setLastOnlineTime(LocalDateTime.now());
        user.setChildren(Collections.emptyList());
        user.setPhones(Set.of(new Phone(1L, +381234567890L, true, user)));

        return user;
    }
}
