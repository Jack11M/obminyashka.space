package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserChangeEmailDto;
import com.hillel.items_exchange.dto.UserChangePasswordDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static com.hillel.items_exchange.model.enums.Status.ACTIVE;
import static com.hillel.items_exchange.model.enums.Status.DELETED;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionParametrizedMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

        assertEquals(getMessageSource("changed.user.password"), message);
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

        assertEquals(getMessageSource("changed.user.email"), message);
        assertEquals(NEW_USER_EMAIL, userWithOldPassword.getEmail());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testDeleteUserFirst_WhenDataCorrect_Successfully() {
        String message = userService.deleteUserFirst(userWithOldPassword);

        assertEquals(getExceptionParametrizedMessageSource("account.deleted.first", numberOfDaysToKeepDeletedUsers),
                message);
        assertEquals(DELETED, userWithOldPassword.getStatus());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testPermanentlyDeleteUsers_ShouldDeleteRequiredUsers() {
        User shouldBeDeleted = createUserForDeleting(DELETED,
                LocalDateTime.now().minusDays(numberOfDaysToKeepDeletedUsers + 1));
        User shouldNotBeDeleted0 = createUserForDeleting(ACTIVE, LocalDateTime.now());
        User shouldNotBeDeleted1 = createUserForDeleting(DELETED,
                LocalDateTime.now().minusDays(numberOfDaysToKeepDeletedUsers - 1));
        User shouldNotBeDeleted2 = createUserForDeleting(ACTIVE,
                LocalDateTime.now().minusDays(numberOfDaysToKeepDeletedUsers + 1));
        List<User> users = List.of(shouldBeDeleted, shouldNotBeDeleted0, shouldNotBeDeleted1, shouldNotBeDeleted2);

        when(userRepository.findAll()).thenReturn(users);
        userService.permanentlyDeleteUsers();

        verify(userRepository).delete(shouldBeDeleted);
        verify(userRepository, never()).delete(shouldNotBeDeleted0);
        verify(userRepository, never()).delete(shouldNotBeDeleted1);
        verify(userRepository, never()).delete(shouldNotBeDeleted2);
    }

    @Test
    void testRestoreUser_WhenDataCorrect_Successfully() {
        String message = userService.restoreUser(userWithOldPassword);

        assertEquals(getExceptionParametrizedMessageSource("account.restored"), message);
        assertEquals(ACTIVE, userWithOldPassword.getStatus());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));
        userWithOldPassword.setUpdated(LocalDateTime.now());

        return userWithOldPassword;
    }

    private User createUserForDeleting(Status status, LocalDateTime updated) {
        User user = new User();
        user.setStatus(status);
        user.setUpdated(updated);

        return user;
    }
}

