package space.obminyashka.items_exchange.rest;

import org.assertj.core.api.Assertions;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.request.ChangeEmailRequest;
import space.obminyashka.items_exchange.rest.request.ChangePasswordRequest;
import space.obminyashka.items_exchange.rest.request.MyUserInfoUpdateRequest;
import space.obminyashka.items_exchange.rest.request.VerifyEmailRequest;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.impl.ImageServiceImpl;
import space.obminyashka.items_exchange.service.impl.UserServiceImpl;
import space.obminyashka.items_exchange.service.util.EmailType;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static space.obminyashka.items_exchange.rest.api.ApiKey.*;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.PositiveMessage.DELETE_ACCOUNT;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.PositiveMessage.RESET_PASSWORD;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;
import static space.obminyashka.items_exchange.util.data_producer.UserDtoCreatingUtil.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest extends BasicControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private MailService mailService;

    @SpyBean
    private ImageServiceImpl imageService;

    @Captor
    private ArgumentCaptor<MultipartFile> captor;

    private static String maxPhonesAmount;

    @Autowired
    public UserControllerIntegrationTest(MockMvc mockMvc, @Value("${max.phones.amount}") String maxPhonesAmount) {
        super(mockMvc);
        UserControllerIntegrationTest.maxPhonesAmount = maxPhonesAmount;
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
                .add(getMessageSource(ExceptionMessage.ILLEGAL_OPERATION))
                .add(getParametrizedMessageSource(DELETE_ACCOUNT, 7L))
                .toString();

        assertTrue(responseContentAsString.contains(expectedErrorMessage));
    }

    @ParameterizedTest
    @WithMockUser
    @MethodSource("listInvalidFields")
    void updateUserInfo_invalidFields_shouldReturnHttpStatusBadRequest(MyUserInfoUpdateRequest userInfoUpdateRequest,
                                                                       String message) throws Exception {
        var mvcResult = sendDtoAndGetMvcResult(put(USER_MY_INFO), userInfoUpdateRequest, status().isBadRequest());

        assertTrue(getResponseContentAsString(mvcResult).contains(message));
    }

    private static Stream<Arguments> listInvalidFields() {
        var userWithInvalidFirstAndLastName = createUserUpdateDtoWithInvalidFirstAndLastName();

        var invalidPhoneAmount = getErrorMessageForInvalidField(INVALID_PHONES_AMOUNT, "{max}", maxPhonesAmount);
        var invalidFirstName = getErrorMessageForInvalidField(INVALID_FIRST_LAST_NAME, "${validatedValue}", userWithInvalidFirstAndLastName.getFirstName());
        var invalidLastName = getErrorMessageForInvalidField(INVALID_FIRST_LAST_NAME, "${validatedValue}", userWithInvalidFirstAndLastName.getLastName());
        var invalidNotNull = getErrorMessageForInvalidField(INVALID_NOT_NULL, "${validatedValue}", "");

        return Stream.of(
                Arguments.of(createUserUpdateDtoWithInvalidAmountOfPhones(), invalidPhoneAmount),
                Arguments.of(createUserUpdateDtoWithInvalidAmountOfPhones(), getMessageSource(INVALID_PHONE_NUMBER)),
                Arguments.of(userWithInvalidFirstAndLastName, invalidFirstName),
                Arguments.of(userWithInvalidFirstAndLastName, invalidLastName),
                Arguments.of(createUserUpdateDtoWithInvalidNullPhone(), invalidNotNull)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void updateUserPassword_whenDataIncorrect_shouldThrowIllegalArgumentException() throws Exception {
        var request = new ChangePasswordRequest(NEW_PASSWORD, WRONG_NEW_PASSWORD_CONFIRMATION);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_PASSWORD), request, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ValidationMessage.DIFFERENT_PASSWORDS)));
    }

    @ParameterizedTest
    @WithMockUser(username = "user")
    @MethodSource("listInvalidEmail")
    void updateUserEmail_whenEmailConfirmationWrong_shouldThrowIllegalArgumentException(String email) throws Exception {
        var changeEmailRequest = new ChangeEmailRequest(email);

        MvcResult mvcResult = sendDtoAndGetMvcResult(put(USER_SERVICE_CHANGE_EMAIL), changeEmailRequest, status().isBadRequest());
        String message = Objects.requireNonNull(mvcResult.getResolvedException()).getMessage();

        assertTrue(message.contains(getMessageSource(ValidationMessage.INVALID_EMAIL)));
    }

    private static Stream<Arguments> listInvalidEmail() {
        return Stream.of(
                Arguments.of(INVALID_EMAIL_WITHOUT_POINT),
                Arguments.of(INVALID_EMAIL_WITHOUT_DOMAIN_NAME)
        );
    }

    @Test
    @WithMockUser
    void resetUserPassword_whenPasswordResetSuccessfully_shouldSendMessage() throws Exception {
        when(userService.existsByEmail(any())).thenReturn(true);
        var verifyEmailRequest = new VerifyEmailRequest("email@gmail.com");

        MvcResult mvcResult = sendDtoAndGetMvcResult(post(USER_SERVICE_RESET_PASSWORD)
                .header(HttpHeaders.HOST, anyString()), verifyEmailRequest, status().isOk());

        assertAll(
                () -> assertEquals(mvcResult.getResponse().getContentAsString(), getMessageSource(RESET_PASSWORD)),
                () -> verify(mailService).sendEmailTemplateAndGenerateConfrimationCode(eq(verifyEmailRequest.email()),
                        eq(EmailType.RESET), any())
        );
    }

    @Test
    @WithMockUser
    void resetUserPassword_whenEmailNotValid_shouldSendMessage() throws Exception {
        var verifyEmailRequest = new VerifyEmailRequest("emailgmail.com");

        MvcResult mvcResult = sendDtoAndGetMvcResult(post(USER_SERVICE_RESET_PASSWORD), verifyEmailRequest, status().isBadRequest());

        Assertions.assertThat(mvcResult.getResolvedException()).hasMessageContaining(getMessageSource(INVALID_EMAIL));
    }

    @Test
    @WithMockUser
    void resetUserPassword_whenEmailNotExist_shouldReturnExceptionMessage() throws Exception {
        var verifyEmailRequest = new VerifyEmailRequest("email@gmail.com");
        when(userService.existsByEmail(verifyEmailRequest.email())).thenReturn(false);

        MvcResult mvcResult = sendDtoAndGetMvcResult(post(USER_SERVICE_RESET_PASSWORD), verifyEmailRequest, status().isOk());
        assertEquals(mvcResult.getResponse().getContentAsString(), getMessageSource(ExceptionMessage.RESET_PASSWORD));
    }

    @Test
    @WithMockUser(username = "admin")
    void setUserAvatar_whenReceivedBMPImage_shouldThrowUnsupportedMediaTypeException() throws Exception {

        MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        sendUriAndGetMvcResult(multipart(new URI(USER_SERVICE_CHANGE_AVATAR)).file(bmp), status().isUnsupportedMediaType());
    }

    @Test
    @WithMockUser
    void updateUserAvatar_whenReceivedSeveralImages_shouldSaveFirstImage() throws Exception {
        MockMultipartFile bmp = new MockMultipartFile("image", "image-bmp.bmp", "image/bmp", "image bmp".getBytes());
        MockMultipartFile jpeg = new MockMultipartFile("image", "test-image.jpeg", MediaType.IMAGE_JPEG_VALUE, "image jpg".getBytes());

        sendUriAndGetMvcResult(multipart(USER_SERVICE_CHANGE_AVATAR).file(jpeg).file(bmp), status().is2xxSuccessful());

        verify(imageService).scale(captor.capture());
        verify(userService).setUserAvatar(eq("user"), any());

        assertEquals(jpeg, captor.getValue());
    }

    @Test
    @WithMockUser("admin")
    void makeAccountActiveAgain_whenUserHasNotStatusDeleted_shouldThrowAccessDenied() throws Exception {

        MvcResult mvcResult = sendUriAndGetMvcResult(put(USER_SERVICE_RESTORE), status().isForbidden());

        String message = mvcResult.getResponse().getContentAsString().trim();
        assertEquals(getMessageSource(ValidationMessage.INVALID_TOKEN), message);
    }

    private String getResponseContentAsString(MvcResult mvcResult) throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString();
    }

    private static String getErrorMessageForInvalidField(String messageFromSource, String replacementValue, String valueToReplace) {
        return getMessageSource(messageFromSource).replace(replacementValue, valueToReplace);
    }
}
