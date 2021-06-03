package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static space.obminyashka.items_exchange.model.enums.Status.ACTIVE;

public class UserDtoCreatingUtil {

    public static final List<ChildDto> EXISTED_CHILDREN = List.of(
            new ChildDto(1L, Gender.MALE, LocalDate.of(2019, 1, 1)),
            new ChildDto(2L, Gender.FEMALE, LocalDate.of(2019, 2, 2)));
    public static final List<ChildDto> NEW_CHILDREN = List.of(
            new ChildDto(0, Gender.MALE, LocalDate.of(2019, 3, 3)));
    public static final Set<PhoneDto> NEW_PHONES = Set.of(
            new PhoneDto(0, "381234567890", true));
    public static final String NEW_USERNAME = "newUsername123";
    public static final String NEW_VALID_EMAIL = "new.admin@gmail.com";
    public static final String NEW_INVALID_DUPLICATE_EMAIL = "test@test.com";
    public static final String OLD_USER_VALID_EMAIL = "user@gmail.com";
    public static final String OLD_ADMIN_VALID_EMAIL = "admin@gmail.com";
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "Мар'яна";
    public static final String NEW_VALID_NAME = "Мар'яна";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "Квітка-Основ'яненко";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";
    public static final byte[] BLANK_AVATAR_IMAGE = "test image png".getBytes();
    public static final String CORRECT_OLD_PASSWORD = "@kuIOIY*h986";
    public static final String WRONG_OLD_PASSWORD = "123456wWWW";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String WRONG_NEW_PASSWORD_CONFIRMATION = "123456qQ";
    private static final int MAX_AMOUNT_OF_PHONES = 5;
    public static final Set<PhoneDto> NEW_INVALID_PHONES = createWithInvalidSizeListOfPhones();

    public static UserUpdateDto createUserUpdateDtoWithInvalidAmountOfPhones() {
        return new UserUpdateDto(NEW_VALID_NAME, NEW_VALID_NAME, NEW_INVALID_PHONES );
    }

    public static UserUpdateDto createUserUpdateDtoWithInvalidFirstAndLastName() {
        return new UserUpdateDto(NEW_INVALID_SHORT_NAME, NEW_INVALID_TWO_WORDS_NAME, NEW_PHONES );
    }

    public static UserDto createUserDtoForUpdatingWithChangedEmailAndFNameApAndLNameMinusWithoutPhones() {
        return getBuild("admin", NEW_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet(), ACTIVE, LocalDateTime.now());
    }

    public static UserDto createUserDtoForUpdatingWithChangedUsernameWithoutPhones() {
        return getBuild(NEW_USERNAME, "admin@gmail.com", Boolean.FALSE, "super",
                "admin", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet(), ACTIVE, LocalDateTime.now());
    }

    public static UserDto createUserDtoForUpdatingWithNewChildAndPhones() {
        return getBuild("new_user", NEW_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                NEW_CHILDREN, NEW_PHONES, ACTIVE, LocalDateTime.now());
    }

    public static UserDto getBuild(String username, String email, Boolean online, String firstName,
                                   String lastName, byte[] avatarImage, LocalDateTime lastOnlineTime,
                                   List<ChildDto> children, Set<PhoneDto> phones, Status status,
                                   LocalDateTime updated) {
        return UserDto.builder()
                .username(username)
                .email(email)
                .online(online)
                .firstName(firstName)
                .lastName(lastName)
                .avatarImage(avatarImage)
                .lastOnlineTime(lastOnlineTime)
                .children(children)
                .phones(phones)
                .status(status)
                .updated(updated)
                .build();
    }

    private static Set<PhoneDto> createWithInvalidSizeListOfPhones() {
        Set<PhoneDto> phoneDto = new HashSet<>();
        for (int i = 0; i <= MAX_AMOUNT_OF_PHONES; i++) {
            phoneDto.add(new PhoneDto(0, "38123456789" + i, true));
        }
        return phoneDto;
    }
}
