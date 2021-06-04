package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Assertions;
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
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.UserService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getJsonOfChildrenDto;
import static space.obminyashka.items_exchange.util.JsonConverter.asJsonString;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

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
    @Value("${max.phones.amount}")
    private String maxPhonesAmount;

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
    void updateUserInfo_badAmountPhones_ReturnHttpStatusBadRequest() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, "/user/info",
                createUserUpdateDtoWithInvalidAmountOfPhones(), status().isBadRequest())
                .andDo(print())
                .andReturn();
        var responseContentAsString = getResponseContentAsString(mvcResult);
        var expectedErrorMessage = getErrorMessageForInvalidField("invalid.phones-amount", "{max}", maxPhonesAmount);
        assertTrue(responseContentAsString.contains(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_invalidFirstAndLastName_shouldReturnHttpStatusBadRequest() throws Exception {
        UserUpdateDto dto = createUserUpdateDtoWithInvalidFirstAndLastName();

        final var errorMessageForInvalidFirstName =
                getErrorMessageForInvalidField("invalid.first-or-last.name", "${validatedValue}", dto.getFirstName());
        final var errorMessageForInvalidLastName =
                getErrorMessageForInvalidField("invalid.first-or-last.name", "${validatedValue}", dto.getLastName());

        MvcResult mvcResult = getResultActions(HttpMethod.PUT, "/user/info", dto, status().isBadRequest())
                .andDo(print())
                .andReturn();
        String responseContentAsString = getResponseContentAsString(mvcResult);
        Assertions.assertAll(
                () ->  assertTrue(responseContentAsString.contains(errorMessageForInvalidFirstName)),
                () ->  assertTrue(responseContentAsString.contains(errorMessageForInvalidLastName))
        );
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
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenOldPasswordWrong_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(WRONG_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.PUT, PATH_USER_CHANGE_PASSWORD, userChangePasswordDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenPasswordConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, WRONG_NEW_PASSWORD_CONFIRMATION);
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
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_VALID_EMAIL, NEW_INVALID_DUPLICATE_EMAIL);
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

        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(OLD_USER_VALID_EMAIL, OLD_USER_VALID_EMAIL);
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

        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(OLD_ADMIN_VALID_EMAIL, OLD_ADMIN_VALID_EMAIL);
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

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
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
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, WRONG_OLD_PASSWORD);
        MvcResult mvcResult = getResultActions(HttpMethod.DELETE, USER_SERVICE_DELETE, userDeleteFlowDto,
                status().isBadRequest())
                .andDo(print())
                .andReturn();
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_WhenPasswordWrongAndPasswordConfirmationDoesNotMatch_ShouldThrowIllegalArgumentException()
            throws Exception {
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, WRONG_OLD_PASSWORD);
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

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
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
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, WRONG_OLD_PASSWORD);
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
        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(WRONG_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
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

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
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

    private String getResponseContentAsString(MvcResult mvcResult) throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString();
    }

    private String getErrorMessageForInvalidField(String messageFromSource, String replacementValue, String valueToReplace) {
        return getMessageSource(messageFromSource).replace(replacementValue, valueToReplace);
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
