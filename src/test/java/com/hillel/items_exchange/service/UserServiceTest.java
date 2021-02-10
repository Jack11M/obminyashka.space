package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserChangeEmailDto;
import com.hillel.items_exchange.dto.UserChangePasswordDto;
import com.hillel.items_exchange.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static com.hillel.items_exchange.model.enums.Status.DELETED;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionParametrizedMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTest {

    public static final String CORRECT_OLD_PASSWORD = "123456xX";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String NEW_USER_EMAIL = "user@mail.ru";

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;
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
        assertTrue(bCryptPasswordEncoder.matches(NEW_PASSWORD, userWithOldPassword.getPassword()));
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
        assertEquals(NEW_USER_EMAIL, userWithOldPassword.getEmail());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testDeleteUser_WhenDataCorrect_Successfully() {
        String message = userService.deleteUser(userWithOldPassword);

        assertEquals(getExceptionParametrizedMessageSource("account.deleted",
                LocalDate.now().plusDays(numberOfDaysToKeepDeletedUsers), userService.getTimeWhenUserShouldBeDeleted()), message);
        assertEquals(DELETED, userWithOldPassword.getStatus());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));

        return userWithOldPassword;
    }
}

