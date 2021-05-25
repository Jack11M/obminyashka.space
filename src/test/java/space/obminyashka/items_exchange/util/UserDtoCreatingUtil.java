package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.enums.Status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static space.obminyashka.items_exchange.model.enums.Status.ACTIVE;

public class UserDtoCreatingUtil {

    public static final byte[] BLANK_AVATAR_IMAGE = "test image png".getBytes();
    private final static LocalDateTime LAST_ONLINE_TIME = LocalDateTime.of(2019, 1, 1, 0, 0, 1);
    public static final String NEW_VALID_EMAIL = "new.admin@gmail.com";
    public static final String NEW_INVALID_DUPLICATE_EMAIL = "test@test.com";
    public static final String OLD_USER_VALID_EMAIL = "user@gmail.com";
    public static final String OLD_ADMIN_VALID_EMAIL = "admin@gmail.com";
    public static final String NEW_VALID_NAME = "Мар'яна";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "Квітка-Основ'яненко";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";
    public static final String CORRECT_OLD_PASSWORD = "@kuIOIY*h986";
    public static final String WRONG_OLD_PASSWORD = "123456wWWW";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String WRONG_NEW_PASSWORD_CONFIRMATION = "123456qQ";
    public static final Set<PhoneDto> NEW_VALID_PHONES = Set.of(
            new PhoneDto(0L, "381234567890", true));
    public static final Set<PhoneDto> NEW_INVALID_PHONES = createInvalidListOfPhones();

    public static UserWithoutChildrenDto createUserDtoForUpdatingWithChangedEmailAndFNameApAndLNameMinus() {
        return getBuild("admin", NEW_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LAST_ONLINE_TIME, NEW_VALID_PHONES, ACTIVE, LocalDateTime.now());
    }

    public static UserWithoutChildrenDto createValidUserWithoutChildrenDtoForUpdating() {
        return getBuild("admin", OLD_ADMIN_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LAST_ONLINE_TIME, NEW_VALID_PHONES, ACTIVE, LocalDateTime.now());
    }
    public static UserWithoutChildrenDto createUserWithoutChildrenDtoForUpdatingWithInvalidAmountOfPhones() {
        return getBuild("admin", OLD_ADMIN_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LAST_ONLINE_TIME, NEW_INVALID_PHONES, ACTIVE, LocalDateTime.now());
    }

    public static UserWithoutChildrenDto createUserWithoutChildrenDtoForUpdatingWithInvalidShortName() {
        return getBuild("admin", OLD_ADMIN_VALID_EMAIL, Boolean.FALSE, NEW_INVALID_SHORT_NAME,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LAST_ONLINE_TIME, NEW_VALID_PHONES, ACTIVE, LocalDateTime.now());
    }

    public static UserWithoutChildrenDto createUserWithoutChildrenDtoForUpdatingWithInvalidFirstName() {
        return getBuild("admin", OLD_ADMIN_VALID_EMAIL, Boolean.FALSE, NEW_INVALID_TWO_WORDS_NAME,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LAST_ONLINE_TIME, Collections.emptySet(), ACTIVE, LocalDateTime.now());
    }

    private static Set<PhoneDto> createInvalidListOfPhones() {
        return Set.of(
                new PhoneDto(0L, "381234567891", true),
                new PhoneDto(0L, "381234567892", true),
                new PhoneDto(0L, "381234567893", true),
                new PhoneDto(0L, "381234567894", true),
                new PhoneDto(0L, "381234567895", true),
                new PhoneDto(0L, "381234567896", true));
    }

    public static UserWithoutChildrenDto getBuild(String username, String email, Boolean online, String firstName,
                                   String lastName, byte[] avatarImage, LocalDateTime lastOnlineTime,
                                   Set<PhoneDto> phones, Status status,
                                   LocalDateTime updated) {
        return UserWithoutChildrenDto.builder()
                .username(username)
                .email(email)
                .online(online)
                .firstName(firstName)
                .lastName(lastName)
                .avatarImage(avatarImage)
                .lastOnlineTime(lastOnlineTime)
                .phones(phones)
                .status(status)
                .updated(updated)
                .build();
    }
}
