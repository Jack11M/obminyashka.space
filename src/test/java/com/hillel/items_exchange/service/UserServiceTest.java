package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserChangeEmailDto;
import com.hillel.items_exchange.dto.UserChangePasswordDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    public static final String CORRECT_OLD_PASSWORD = "123456xX";
    public static final String WRONG_OLD_PASSWORD = "123456wWWW";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String WRONG_NEW_PASSWORD_CONFIRMATION = "123456qQ";
    public static final String NEW_USER_EMAIL = "user@mail.ru";
    public static final String WRONG_USER_EMAIL_CONFIRMATION = "user@gmail.com";

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User userWithOldPassword;

    @BeforeEach
    void setUp() {
        userWithOldPassword = createUserWithOldPassword();
    }

    @Test
    void testUpdateUserPassword_Successfully() throws InvalidDtoException {
        //given
        UserChangePasswordDto userChangePasswordDto =
                createUserChangePasswordDtoWithCorrectData();
        User userWithNewPassword = createUserWithNewPassword();
        //when
        when(userRepository.saveAndFlush(userWithOldPassword)).thenReturn(userWithNewPassword);
        UserDto userDto = userService.updateUserPassword(userChangePasswordDto, userWithOldPassword);
        //then
        assertEquals(userDto.getUsername(), userWithNewPassword.getUsername());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testUpdateUserPassword_WhenOldPasswordWrong_ShouldThrowInvalidDtoException() {
        //given
        UserChangePasswordDto userChangePasswordDto =
                createUserChangePasswordDtoWithWrongOldPassword();
        //then
        InvalidDtoException invalidDtoException = assertThrows(InvalidDtoException.class,
                () -> userService.updateUserPassword(userChangePasswordDto, userWithOldPassword));
        assertTrue(invalidDtoException.getMessage().contains(
                "Incorrect password. Please, check if you entered password correctly"));
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void testUpdateUserPassword_WhenPasswordConfirmationWrong_ShouldThrowIllegalArgumentException() {
        //given
        UserChangePasswordDto userChangePasswordDto =
                createUserChangePasswordDtoWithWrongPasswordConfirmation();
        //then
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserPassword(userChangePasswordDto, userWithOldPassword));
    }

    @Test
    void testUpdateUserEmail_Successfully() {
        //given
        UserChangeEmailDto userChangeEmailDto =
                createUserChangeEmailDtoWithCorrectData();
        User userWithNewEmail = createUserWithNewEmail();
        //when
        when(userRepository.saveAndFlush(userWithOldPassword)).thenReturn(userWithNewEmail);
        UserDto userDto = userService.updateUserEmail(userChangeEmailDto, userWithOldPassword);
        //then
        assertEquals(userDto.getUsername(), userWithNewEmail.getUsername());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testUpdateUserEmail_WhenEmailConfirmationWrong_ShouldThrowIllegalArgumentException() {
        //given
        UserChangeEmailDto userChangeEmailDto =
                createUserChangeEmailDtoWithWrongEmailConfirmation();
        //then
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserEmail(userChangeEmailDto, userWithOldPassword));
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));

        return userWithOldPassword;
    }

    private User createUserWithNewPassword() {
        User user = new User();
        user.setPassword(NEW_PASSWORD);

        return user;
    }

    private UserChangePasswordDto createUserChangePasswordDtoWithCorrectData() {

        return createUserChangePasswordDto(CORRECT_OLD_PASSWORD,
                NEW_PASSWORD,
                NEW_PASSWORD);
    }

    private UserChangePasswordDto createUserChangePasswordDtoWithWrongOldPassword() {

        return createUserChangePasswordDto(WRONG_OLD_PASSWORD,
                NEW_PASSWORD,
                NEW_PASSWORD);
    }

    private UserChangePasswordDto createUserChangePasswordDtoWithWrongPasswordConfirmation() {

        return createUserChangePasswordDto(CORRECT_OLD_PASSWORD,
                NEW_PASSWORD,
                WRONG_NEW_PASSWORD_CONFIRMATION);
    }

    private UserChangePasswordDto createUserChangePasswordDto(String password,
                                                              String newPassword,
                                                              String confirmNewPassword) {
        return new UserChangePasswordDto(password, newPassword, confirmNewPassword);
    }

    private User createUserWithNewEmail() {
        User user = new User();
        user.setEmail(NEW_USER_EMAIL);

        return user;
    }

    private UserChangeEmailDto createUserChangeEmailDtoWithCorrectData() {

        return createUserChangeEmailDto(NEW_USER_EMAIL, NEW_USER_EMAIL);
    }

    private UserChangeEmailDto createUserChangeEmailDtoWithWrongEmailConfirmation() {
        return createUserChangeEmailDto(NEW_USER_EMAIL, WRONG_USER_EMAIL_CONFIRMATION);
    }

    private UserChangeEmailDto createUserChangeEmailDto(String newEmail,
                                                        String newEmailConfirmation) {
        return new UserChangeEmailDto(newEmail, newEmailConfirmation);
    }
}
