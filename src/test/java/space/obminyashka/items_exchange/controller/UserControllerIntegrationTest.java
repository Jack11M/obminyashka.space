package space.obminyashka.items_exchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.BasicControllerTest;
import space.obminyashka.items_exchange.dto.UserDeleteFlowDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.impl.ImageServiceImpl;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @SpyBean
    private ImageServiceImpl imageService;

    private User user;

    @Captor
    private ArgumentCaptor<MultipartFile> captor;

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
    void updateUserInfo_whenUserHasStatusDeleted_shouldReturn403WithSpecificMessage() throws Exception {
        when(userService.getDaysBeforeDeletion(any())).thenReturn(7L);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDto(), status().isForbidden());
        var responseContentAsString = getResponseContentAsString(mvcResult);
        var expectedErrorMessage = new StringJoiner(". ")
                .add(getMessageSource(ResponseMessagesHandler.ExceptionMessage.ILLEGAL_OPERATION))
                .add(getParametrizedMessageSource(ResponseMessagesHandler.PositiveMessage.DELETE_ACCOUNT, 7L))
                .toString();

        assertTrue(responseContentAsString.contains(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_badAmountPhones_shouldReturnHttpStatusBadRequest() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), createUserUpdateDtoWithInvalidAmountOfPhones(), status().isBadRequest());
        var responseContentAsString = getResponseContentAsString(mvcResult);
        var expectedErrorMessage = getErrorMessageForInvalidField(
                ResponseMessagesHandler.ValidationMessage.INVALID_PHONES_AMOUNT, "{max}", maxPhonesAmount);
        assertTrue(responseContentAsString.contains(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateUserInfo_invalidFirstAndLastName_shouldReturnHttpStatusBadRequest() throws Exception {
        UserUpdateDto dto = createUserUpdateDtoWithInvalidFirstAndLastName();

        final var errorMessageForInvalidFirstName = getErrorMessageForInvalidField(
                ResponseMessagesHandler.ValidationMessage.INVALID_FIRST_LAST_NAME, "${validatedValue}", dto.getFirstName());
        final var errorMessageForInvalidLastName = getErrorMessageForInvalidField(
                ResponseMessagesHandler.ValidationMessage.INVALID_FIRST_LAST_NAME, "${validatedValue}", dto.getLastName());

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), dto, status().isBadRequest());
        String responseContentAsString = getResponseContentAsString(mvcResult);
        assertAll(
                () -> assertTrue(responseContentAsString.contains(errorMessageForInvalidFirstName)),
                () -> assertTrue(responseContentAsString.contains(errorMessageForInvalidLastName))
        );
    }

    @Test
    @WithMockUser(username = "admin")
    void addChild_badTotalAmount_shouldReturnBadRequest() throws Exception {
        var badTotalAmountChildDto = generateTestChildren(MAX_CHILDREN_AMOUNT + 1);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), badTotalAmountChildDto, status().isBadRequest());
        final var expectedErrorMessage = getParametrizedMessageSource(ResponseMessagesHandler.ExceptionMessage.CHILDREN_AMOUNT)
                .replace("{max}", String.valueOf(MAX_CHILDREN_AMOUNT));
        assertTrue(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage().contains(expectedErrorMessage));
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserPassword_whenDataIncorrect_shouldThrowIllegalArgumentException() throws Exception {
        MvcResult mvcResult = sendUriAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD).
                param("password", NEW_PASSWORD).
                param("confirmPassword", WRONG_NEW_PASSWORD_CONFIRMATION), status().isBadRequest());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS)));
    }

    @ParameterizedTest
    @WithMockUser(username = "user")
    @MethodSource("listInvalidEmail")
    void updateUserEmail_whenEmailConfirmationWrong_shouldThrowIllegalArgumentException(String email) throws Exception {
        MvcResult mvcResult = sendUriAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL).param("email", email), status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL)));
    }

    private static Stream<Arguments> listInvalidEmail() {
        return Stream.of(
                Arguments.of(INVALID_EMAIL_WITHOUT_POINT),
                Arguments.of(INVALID_EMAIL_WITHOUT_DOMAIN_NAME)
        );
    }

    @ParameterizedTest
    @WithMockUser(username = "user")
    @MethodSource("listOldEmail")
    void updateUserEmail_whenUserEnteredOldEmail_shouldThrowDataConflictException(String email, boolean isExists) throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));
        when(userService.existsByEmail(any())).thenReturn(isExists);

        sendUriAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL).param("email", email), status().isConflict());
    }

    private static Stream<Arguments> listOldEmail() {
        return Stream.of(
                Arguments.of(OLD_ADMIN_VALID_EMAIL, true),
                Arguments.of(OLD_USER_VALID_EMAIL, false)
        );
    }

    @Test
    @WithMockUser(username = "admin")
    void setUserAvatar_whenReceivedBMPImage_shouldThrowUnsupportedMediaTypeException() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        sendUriAndGetMvcResult(multipart(new URI(USER_SERVICE_CHANGE_AVATAR)).file(bmp), status().isUnsupportedMediaType());
    }

    @Test
    @WithMockUser
    void setUserAvatar_whenReceivedSeveralImages_shouldSaveFirstImage() throws Exception {
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        MockMultipartFile jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE, "image jpg".getBytes());

        sendUriAndGetMvcResult(multipart(USER_SERVICE_CHANGE_AVATAR).file(jpeg).file(bmp), status().is2xxSuccessful());

        verify(imageService).scale(captor.capture());
        verify(userService).findByUsernameOrEmail(any());

        assertEquals(jpeg, captor.getValue());
    }

    @Test
    @WithMockUser(username = "admin")
    void selfDeleteRequest_whenPasswordWrongAndTheSameConfirmation_shouldThrowInvalidDtoException() throws Exception {
        MvcResult mvcResult = selfDeleteRequestBasicTest(false, WRONG_OLD_PASSWORD, delete(USER_SERVICE_DELETE), status().isBadRequest());

        assertTrue(mvcResult.getResponse().getContentAsString().contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.INCORRECT_PASSWORD)));
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
    void selfDeleteRequest_whenPasswordCorrectAndConfirmationWrong_shouldThrowIllegalArgumentException(UserDeleteFlowDto testDto) throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(delete(USER_SERVICE_DELETE), testDto, status().isBadRequest());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS)));
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
    void makeAccountActiveAgain_whenPasswordCorrectAndConfirmationWrong_shouldThrowIllegalArgumentException(UserDeleteFlowDto testDto) throws Exception {
        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_RESTORE), testDto, status().isBadRequest());

        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS)));
    }

    @Test
    @WithMockUser("admin")
    void makeAccountActiveAgain_whenUserHasNotStatusDeleted_shouldThrowAccessDenied() throws Exception {
        MvcResult mvcResult = selfDeleteRequestBasicTest(true, CORRECT_OLD_PASSWORD, put(USER_SERVICE_RESTORE), status().isForbidden());

        String message = mvcResult.getResponse().getContentAsString().trim();
        assertEquals(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_TOKEN), message);
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
    void addChild_invalidChildAge_shouldReturnHttpStatusBadRequest() throws Exception {
        var childDto = getTestChildren(2001);
        when(userService.findByUsernameOrEmail(any())).thenReturn(Optional.of(user));

        final MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_CHILD), childDto, status().isBadRequest());
        final var resolvedException = mvcResult.getResolvedException();
        assertAll(
                () -> assertNotNull(resolvedException),
                () -> assertNotNull(resolvedException.getMessage()),
                () -> assertTrue(resolvedException.getMessage()
                        .contains(getParametrizedMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_CHILD_AGE))));
    }
}
