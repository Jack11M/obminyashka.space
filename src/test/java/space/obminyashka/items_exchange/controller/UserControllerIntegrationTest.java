package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.api.ApiKey.*;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.generateTestChildren;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getTestChildren;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest extends BasicControllerTest {

    @MockBean
    private UserServiceImpl userService;

    private User user;

    private static final int MAX_CHILDREN_AMOUNT = 10;
    @Value("${max.phones.amount}")
    private String maxPhonesAmount;

    @Autowired
    public UserControllerIntegrationTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    void negativeTestReceivingInformationAboutAnotherUser() throws Exception {
        sendUriAndGetMvcResult(get(USER_MY_INFO), status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "deletedUser", roles = "SELF_REMOVING")
    void updateUserInfo_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        when(userService.getDaysBeforeDeletion(any())).thenReturn(7L);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDto(), status().isForbidden());
        var responseContentAsString = getResponseContentAsString(mvcResult);
        var expectedErrorMessage = new StringJoiner(". ")
                .add(getMessageSource("exception.illegal.operation"))
                .add(getParametrizedMessageSource("account.self.delete.request", 7L))
                .toString();

        assertTrue(responseContentAsString.contains(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_badAmountPhones_ReturnHttpStatusBadRequest() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDtoWithInvalidAmountOfPhones(), status().isBadRequest());
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

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), dto, status().isBadRequest());
        String responseContentAsString = getResponseContentAsString(mvcResult);
        assertAll(
                () -> assertTrue(responseContentAsString.contains(errorMessageForInvalidFirstName)),
                () -> assertTrue(responseContentAsString.contains(errorMessageForInvalidLastName))
        );
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_BadTotalAmount_ShouldReturnBadRequest() throws Exception {
        var badTotalAmountChildDto = generateTestChildren(MAX_CHILDREN_AMOUNT + 1);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), badTotalAmountChildDto, status().isBadRequest());
        final var expectedErrorMessage = getParametrizedMessageSource("exception.children-amount")
                .replace("{max}", String.valueOf(MAX_CHILDREN_AMOUNT));
        assertTrue(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage().contains(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenOldPasswordWrong_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(WRONG_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isBadRequest());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    @Test
    @WithMockUser()
    void updateUserPassword_whenUserEnteredOldPassword_shouldThrowIllegalArgumentException() throws Exception {
        var userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("same.passwords")));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenPasswordConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, WRONG_NEW_PASSWORD_CONFIRMATION);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserPassword_EmailInvalidFormat_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(true);
        user.setEmail(INVALID_EMAIL);

        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isConflict());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("invalid.email")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenEmailConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        updateUserEmailBasicTest(NEW_VALID_EMAIL, NEW_INVALID_DUPLICATE_EMAIL, status().isBadRequest(), "invalid.confirm.email");
    }

    private void updateUserEmailBasicTest(String email, String confirmationEmail, ResultMatcher expectedStatus, String expectedExceptionKey) throws Exception {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(email, confirmationEmail);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), userChangeEmailDto, expectedStatus);
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(expectedExceptionKey)));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenUserEnteredOldEmail_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        updateUserEmailBasicTest(OLD_USER_VALID_EMAIL, OLD_USER_VALID_EMAIL, status().isConflict(), "exception.email.old");
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenUserEnteredExistedEmail_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.existsByEmail(any())).thenReturn(true);

        updateUserEmailBasicTest(OLD_ADMIN_VALID_EMAIL, OLD_ADMIN_VALID_EMAIL, status().isConflict(), "email.duplicate");
    }

    @Test
    @WithMockUser(username = "admin")
    void setUserAvatar_whenReceivedBMPImage_shouldThrowUnsupportedMediaTypeException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        sendUriAndGetMvcResult(multipart(new URI(USER_SERVICE_CHANGE_AVATAR)).file(bmp), status().isUnsupportedMediaType());
    }

    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_WhenPasswordWrongAndTheSameConfirmation_ShouldThrowInvalidDtoException() throws Exception {
        MvcResult mvcResult = selfDeleteRequestBasicTest(false, WRONG_OLD_PASSWORD, delete(USER_SERVICE_DELETE), status().isBadRequest());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    private MvcResult selfDeleteRequestBasicTest(boolean isUsernameExists, String oldPassword, MockHttpServletRequestBuilder request, ResultMatcher expectedResult) throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(isUsernameExists);

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(oldPassword, oldPassword);
        return sendDtoAndGetMvcResult(request, userDeleteFlowDto, expectedResult);
    }

    @ParameterizedTest
    @WithMockUser(username = "admin")
    @MethodSource("selfDeleteTestData")
    void selfDeleteRequest_WhenPasswordCorrectAndConfirmationWrong_ShouldThrowIllegalArgumentException(UserDeleteFlowDto testDto) throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(delete(USER_SERVICE_DELETE), testDto, status().isBadRequest());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    private static Stream<Arguments> selfDeleteTestData() {
        return Stream.of(
                Arguments.of(new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, WRONG_OLD_PASSWORD)),
                Arguments.of(new UserDeleteFlowDto(WRONG_OLD_PASSWORD, CORRECT_OLD_PASSWORD))
        );
    }

    @ParameterizedTest
    @WithMockUser(username = "deletedUser", roles = "SELF_REMOVING")
    @MethodSource("selfDeleteTestData")
    void makeAccountActiveAgain_WhenPasswordCorrectAndConfirmationWrong_ShouldThrowIllegalArgumentException(UserDeleteFlowDto testDto) throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_RESTORE), testDto, status().isBadRequest());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser("admin")
    void makeAccountActiveAgain_WhenUserHasNotStatusDeletedShouldThrowAccessDenied() throws Exception {
        MvcResult mvcResult = selfDeleteRequestBasicTest(true, CORRECT_OLD_PASSWORD, put(USER_SERVICE_RESTORE), status().isForbidden());

        String message = mvcResult.getResponse().getContentAsString().trim();
        assertEquals(getMessageSource("invalid.token"), message);
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
        user.setPhones(Set.of(new Phone(UUID.randomUUID(), +381234567890L, true, user)));

        return user;
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_InvalidChildAge_ShouldReturnHttpStatusBadRequest() throws Exception {
        var childDto = getTestChildren(2001);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), childDto, status().isBadRequest());
        final var resolvedException = mvcResult.getResolvedException();
        assertAll(
                () -> assertNotNull(resolvedException),
                () -> assertNotNull(resolvedException.getMessage()),
                () -> assertTrue(resolvedException.getMessage()
                        .contains(getParametrizedMessageSource("invalid.child.age"))));
    }
}
