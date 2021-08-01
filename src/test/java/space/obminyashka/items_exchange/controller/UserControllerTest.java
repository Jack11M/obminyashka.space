package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.Assertions;
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
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.generateTestChildren;
import static space.obminyashka.items_exchange.util.ChildDtoCreatingUtil.getTestChildren;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.UserDtoCreatingUtil.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends BasicControllerTest {

    @MockBean
    private UserServiceImpl userService;

    private User user;

    private static int maxChildrenAmount;
    @Value("${max.phones.amount}")
    private String maxPhonesAmount;

    @Autowired
    public UserControllerTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Value("${max.children.amount}")
    public void setMaxChildrenAmount(int maxChildrenAmount) {
        UserControllerTest.maxChildrenAmount = maxChildrenAmount;
    }

    @Test
    void negativeTestReceivingInformationAboutAnotherUser() throws Exception {
        sendUriAndGetMvcResult(get(USER_MY_INFO), status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "deletedUser")
    void updateUserInfo_WhenUserHasStatusDeleted_ShouldReturn403WithSpecificMessage() throws Exception {
        user.setStatus(Status.DELETED);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.getDaysBeforeDeletion(any())).thenReturn(7L);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDto(), status().isForbidden());
        var responseContentAsString = getResponseContentAsString(mvcResult);
        var expectedErrorMessage = new StringJoiner(". ")
                .add(getMessageSource("exception.illegal.operation"))
                .add(getParametrizedMessageSource("account.self.delete.request", userService.getDaysBeforeDeletion(user)))
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
        Assertions.assertAll(
                () -> assertTrue(responseContentAsString.contains(errorMessageForInvalidFirstName)),
                () -> assertTrue(responseContentAsString.contains(errorMessageForInvalidLastName))
        );
    }

    @ParameterizedTest
    @WithMockUser(username = "admin")
    @MethodSource("userIdentifierTestData")
    void addChild_NotValidDto_ShouldThrowIllegalIdentifierException(List<ChildDto> testChildren, String expectedErrorMessageKey) throws Exception {

        final MvcResult mvcResult = sendDtoAndGetMvcResult(post(USER_CHILD), testChildren, status().isBadRequest());
        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(expectedErrorMessageKey)));
    }

    private static Stream<Arguments> userIdentifierTestData() {
        return Stream.of(
                Arguments.of(getTestChildren(111L, 222L, 2019), "invalid.new.entity.id"),
                Arguments.of(generateTestChildren(maxChildrenAmount + 1), "exception.invalid.dto")
        );
    }

    @Test
    @WithMockUser(username = "admin")
    void updateChild_NotValidDto_ShouldThrowIllegalIdentifierException() throws Exception {
        var notValidUpdatingChildDto = getTestChildren(1L, 999L, 2018);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), notValidUpdatingChildDto, status().isBadRequest());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Not all children from dto present in"));
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_BadTotalAmount_ShouldThrowEntityAmountException() throws Exception {
        var badTotalAmountChildDto = generateTestChildren(maxChildrenAmount - 1);
        user.setChildren(List.of(
                new Child(1L, Gender.MALE, LocalDate.of(2019, 1, 1), user),
                new Child(2L, Gender.FEMALE, LocalDate.of(2019, 1, 1), user)));
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = sendDtoAndGetMvcResult(post(USER_CHILD), badTotalAmountChildDto, status().isNotAcceptable());
        assertTrue(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage()
                .contains(getParametrizedMessageSource("exception.children-amount", maxChildrenAmount)));
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
    @WithMockUser(username = "admin")
    void updateUserPassword_WhenPasswordConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, WRONG_NEW_PASSWORD_CONFIRMATION);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), userChangePasswordDto, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenEmailConfirmationWrong_ShouldThrowIllegalArgumentException() throws Exception {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_VALID_EMAIL, NEW_INVALID_DUPLICATE_EMAIL);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), userChangeEmailDto, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("invalid.confirm.email")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenUserEnteredOldEmail_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(OLD_USER_VALID_EMAIL, OLD_USER_VALID_EMAIL);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), userChangeEmailDto, status().isConflict());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("exception.email.old")));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserEmail_WhenUserEnteredExistedEmail_ShouldThrowDataConflictException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.existsByEmail(any())).thenReturn(true);

        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(OLD_ADMIN_VALID_EMAIL, OLD_ADMIN_VALID_EMAIL);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), userChangeEmailDto, status().isConflict());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("email.duplicate")));
    }

    @Test
    @WithMockUser(username = "admin")
    void setUserAvatar_whenReceivedBMPImage_shouldThrowUnsupportedMediaTypeException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        MockMultipartFile bmp = new MockMultipartFile("file", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        sendUriAndGetMvcResult(multipart(USER_SERVICE_CHANGE_AVATAR).file(bmp), status().isUnsupportedMediaType());
    }

    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_WhenPasswordWrongAndTheSameConfirmation_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(delete(USER_SERVICE_DELETE), userDeleteFlowDto, status().isBadRequest());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
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

    @Test
    @WithMockUser(username = "deletedUser")
    void makeAccountActiveAgain_WhenPasswordWrongAndTheSameConfirmation_ShouldThrowInvalidDtoException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(false);

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(delete(USER_SERVICE_DELETE), userDeleteFlowDto, status().isBadRequest());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource("incorrect.password")));
    }

    @ParameterizedTest
    @WithMockUser(username = "deletedUser")
    @MethodSource("selfDeleteTestData")
    void makeAccountActiveAgain_WhenPasswordCorrectAndConfirmationWrong_ShouldThrowIllegalArgumentException(UserDeleteFlowDto testDto) throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_RESTORE), testDto, status().isBadRequest());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("different.passwords")));
    }

    @Test
    @WithMockUser("admin")
    void makeAccountActiveAgain_WhenUserHasNotStatusDeletedShouldThrowIllegalOperationException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.isPasswordMatches(any(), any())).thenReturn(true);

        UserDeleteFlowDto userDeleteFlowDto = new UserDeleteFlowDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_RESTORE), userDeleteFlowDto, status().isForbidden());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource("exception.illegal.operation")));
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
