package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.dto.UserChangeEmailDto;
import space.obminyashka.items_exchange.dto.UserChangePasswordDto;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static space.obminyashka.items_exchange.model.enums.Status.ACTIVE;
import static space.obminyashka.items_exchange.model.enums.Status.DELETED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
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
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(CORRECT_OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);
        String message = userService.updateUserPassword(userChangePasswordDto, userWithOldPassword);

        assertEquals(getMessageSource("changed.user.password"), message);
        assertTrue(bCryptPasswordEncoder.matches(NEW_PASSWORD, userWithOldPassword.getPassword()));
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testUpdateUserEmail_WhenDataCorrect_Successfully() {
        UserChangeEmailDto userChangeEmailDto = new UserChangeEmailDto(NEW_USER_EMAIL, NEW_USER_EMAIL);
        String message = userService.updateUserEmail(userChangeEmailDto, userWithOldPassword);

        assertEquals(getMessageSource("changed.user.email"), message);
        assertEquals(NEW_USER_EMAIL, userWithOldPassword.getEmail());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testSelfDeleteRequest_WhenDataCorrect_Successfully() {
        userService.selfDeleteRequest(userWithOldPassword);

        assertEquals(DELETED, userWithOldPassword.getStatus());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    @Test
    void testPermanentlyDeleteUsers_ShouldDeleteRequiredUsers() {
        List<User> users = createTestUsers();
        assertEquals(4, users.size());

        when(userRepository.findAll()).thenReturn(users);
        userService.permanentlyDeleteUsers();

        verify(userRepository).delete(users.get(0));

        for (int i = 1; i < users.size(); i++) {
            verify(userRepository, never()).delete(users.get(i));
        }
    }

    @Test
    void makeAccountActiveAgain_WhenDataCorrect_Successfully() {
        userService.makeAccountActiveAgain(userWithOldPassword);

        assertEquals(ACTIVE, userWithOldPassword.getStatus());
        verify(userRepository).saveAndFlush(userWithOldPassword);
    }

    private User createUserWithOldPassword() {
        userWithOldPassword = new User();
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(CORRECT_OLD_PASSWORD));
        userWithOldPassword.setUpdated(LocalDateTime.now());

        return userWithOldPassword;
    }

    private List<User> createTestUsers() {
        User shouldBeDeleted = createUserForDeleting(DELETED, numberOfDaysToKeepDeletedUsers + 1);
        User shouldNotBeDeleted0 = createUserForDeleting(ACTIVE, 0);
        User shouldNotBeDeleted1 = createUserForDeleting(DELETED, numberOfDaysToKeepDeletedUsers - 1);
        User shouldNotBeDeleted2 = createUserForDeleting(ACTIVE, numberOfDaysToKeepDeletedUsers + 1);

        return List.of(shouldBeDeleted, shouldNotBeDeleted0, shouldNotBeDeleted1, shouldNotBeDeleted2);
    }

    private User createUserForDeleting(Status status, int delay) {
        User user = new User();
        user.setStatus(status);
        user.setUpdated(LocalDateTime.now().minusDays(delay));

        return user;
    }
}
