package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.PhoneDto;
import com.hillel.items_exchange.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

public class UserDtoCreatingUtil {

    public static final Set<ChildDto> EXISTED_CHILDREN = Set.of(
            new ChildDto(1L, "male", LocalDate.of(2019, 1, 1)),
            new ChildDto(2L, "female", LocalDate.of(2019, 2, 2)));
    public static final String NEW_USERNAME = "newUsername123";
    public static final String NEW_VALID_EMAIL = "new.admin@gmail.com";
    public static final String NEW_INVALID_DUPLICATE_EMAIL = "test@test.com";
    public static final LocalDateTime NEW_LAST_ONLINE_TIME = LocalDateTime.now();
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "Мар'яна";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "Квітка-Основ'яненко";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";

    public static UserDto createUserDtoForUpdatingWithChangedEmailAndFNameApAndLNameMinusWithoutPhones() {
        return getBuild("admin", NEW_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChangedUsernameWithoutPhones() {
        return getBuild(NEW_USERNAME, "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChangedLastOnlineTimeWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", NEW_LAST_ONLINE_TIME,
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChangedChildrenWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                Collections.emptySet(), Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Set.of(new PhoneDto(1L, true, "+381234567890", true)));
    }

    public static UserDto createUserDtoForUpdatingWithInvalidShortFNameWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_SHORT_NAME,
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithInvalidLNameWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_TWO_WORDS_NAME,
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithDuplicateEmailWithoutPhones() {
        return getBuild("admin", NEW_INVALID_DUPLICATE_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto getBuild(String username, String email, Boolean online, String firstName,
                                   String lastName, String avatarImage, LocalDateTime lastOnlineTime,
                                   Set<ChildDto> children, Set<PhoneDto> phones) {
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
                .build();
    }
}
