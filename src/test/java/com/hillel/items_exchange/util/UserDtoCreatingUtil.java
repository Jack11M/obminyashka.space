package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.UserDto;

import java.time.LocalDate;

public class UserDtoCreatingUtil {

    public static final long NOT_USER_ID = 100L;
    public static final String NEW_USERNAME = "newUsername123";
    public static final String NEW_EMAIL = "new.admin@gmail.com";
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "new'Name";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "new-Name";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";

    public static UserDto createUserDtoForUpdatingWithChangedEmailAndFirstNameApostrAndLastNameMinus() {
        return getBuild(1L, "admin", NEW_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, "empty", LocalDate.of(2019, 1, 1));
    }

    public static UserDto createUserDtoForUpdatingWithChangedUsername() {
        return getBuild(1L, NEW_USERNAME, "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", LocalDate.of(2019, 1, 1));
    }

    public static UserDto createUserDtoForUpdatingWithInvalidShortFirstName() {
        return getBuild(1L, "admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_SHORT_NAME,
                "admin", "empty", LocalDate.of(2019, 1, 1));
    }

    public static UserDto createUserDtoForUpdatingWithInvalidLastName() {
        return getBuild(1L, "admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_TWO_WORDS_NAME,
                "admin", "empty", LocalDate.of(2019, 1, 1));
    }

    public static UserDto createUserDtoForUpdatingWithNotUserId() {
        return getBuild(NOT_USER_ID, "admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", LocalDate.of(2019, 1, 1));
    }

    public static UserDto getBuild(long id, String username, String email, Boolean online, String firstName,
                                   String lastName, String avatarImage, LocalDate lastOnlineTime) {
        return UserDto.builder()
                .id(id)
                .username(username)
                .email(email)
                .online(online)
                .firstName(firstName)
                .lastName(lastName)
                .avatarImage(avatarImage)
                .lastOnlineTime(lastOnlineTime)
                .build();
    }
}
