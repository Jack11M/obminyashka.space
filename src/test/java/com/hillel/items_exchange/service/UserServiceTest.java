package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserDeleteDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.dto.UserChangeEmailDto;
import com.hillel.items_exchange.dto.UserChangePasswordDto;
import com.hillel.items_exchange.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.hillel.items_exchange.model.Status.ACTIVE;
import static com.hillel.items_exchange.model.Status.DELETED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTest {

    public static final String CORRECT_OLD_PASSWORD = "123456xX";
    public static final String WRONG_OLD_PASSWORD = "123456wWWW";
    public static final String USERNAME = "Trump";
    public static final String PASSWORD_FOR_DELETED_USERS = "eyJ74zdW!Ii51$?)@OiDEyMz*Q1kBt";
    public static final String ENCODED_PASSWORD_FOR_DELETED_USER =
            "$2a$10$N5cy4BWuSntHGMj3UkO9C.QTNTbF6pEFMohYVxKULUKXQJAoDOJP2";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String NEW_USER_EMAIL = "user@mail.ru";

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
    void testUpdateUserPassword_WhenDataCorrect_Successfully() {
        UserChangePasswordDto userChangePasswordDto = UserChangePasswordDto.builder()
                .oldPassword(CORRECT_OLD_PASSWORD)
                .newPassword(NEW_PASSWORD)
                .confirmNewPassword(NEW_PASSWORD)
                .build();
        String message = userService.updateUserPassword(userChangePasswordDto, userWithOldPassword);

        assertEquals(getMessageSource("password.changed"), message);
    void testDeleteUser_Successfully() throws InvalidDtoException {
        //given
        UserDeleteDto userDeleteDto = createUserDeleteDtoWithCorrectData();
        User deletedUser = createDeletedUser();
        //when
        when(userRepository.saveAndFlush(userWithOldPassword)).thenReturn(deletedUser);
        UserDto userDto = userService.deleteUser(userDeleteDto, userWithOldPassword);
        //then
        assertEquals(userDto.getUsername(), deletedUser.getUsername());
        assertTrue(bCryptPasswordEncoder.matches(deletedUser.getPassword(),
                ENCODED_PASSWORD_FOR_DELETED_USER));
        assertEquals(DELETED, userWithOldPassword.getStatus());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testUpdateUserEmail_WhenDataCorrect_Successfully() {
        UserChangeEmailDto userChangeEmailDto = UserChangeEmailDto.builder()
                .newEmail(NEW_USER_EMAIL)
                .newEmailConfirmation(NEW_USER_EMAIL)
                .build();
        String message = userService.updateUserEmail(userChangeEmailDto, userWithOldPassword);

        assertEquals(getMessageSource("email.changed"), message);
        verify(userRepository).saveAndFlush(userWithOldPassword);
    void testDeleteUser_WhenOldPasswordWrong_ShouldThrowInvalidDtoException() {
        //given
        UserDeleteDto userDeleteDto =
                createDeleteUserDtoWithWrongOldPassword();
        //then
        InvalidDtoException invalidDtoException = assertThrows(InvalidDtoException.class,
                () -> userService.deleteUser(userDeleteDto, userWithOldPassword));
        assertTrue(invalidDtoException.getMessage().contains(
                "Incorrect password. Please, check if you entered password correctly"));
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void testDeleteUser_WhenPasswordConfirmationWrong_ShouldThrowIllegalArgumentException() {
        //given
        UserDeleteDto userDeleteDto =
                createDeleteUserDtoWithWrongPasswordConfirmation();
        //then
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(userDeleteDto, userWithOldPassword));
        assertTrue(illegalArgumentException.getMessage().contains("source cannot be null"));
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();

        userWithOldPassword.setUsername(USERNAME);
        userWithOldPassword.setStatus(ACTIVE);
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));

        return userWithOldPassword;
    }

    private User createDeletedUser() {
        User deletedUser = new User();

        deletedUser.setUsername(USERNAME);
        deletedUser.setStatus(DELETED);
        deletedUser.setPassword(PASSWORD_FOR_DELETED_USERS);

        return deletedUser;
    }

    private UserDeleteDto createUserDeleteDtoWithCorrectData() {

        return new UserDeleteDto(CORRECT_OLD_PASSWORD, CORRECT_OLD_PASSWORD);
    }

    private UserDeleteDto createDeleteUserDtoWithWrongOldPassword() {

        return new UserDeleteDto(WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
    }

    private UserDeleteDto createDeleteUserDtoWithWrongPasswordConfirmation() {

        return new UserDeleteDto(CORRECT_OLD_PASSWORD, WRONG_OLD_PASSWORD);
    }
}
