package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.UserDto;

import java.time.LocalDateTime;

public class UserDtoCreatingUtil {

    public static final String NEW_USERNAME = "newUsername123";
    public static final String NEW_EMAIL = "new.admin@gmail.com";
    public static final LocalDateTime NEW_LAST_ONLINE_TIME = LocalDateTime.now();
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "new'Name";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "new-Name";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";

    public static UserDto createUserDtoForUpdatingWithChangedEmailAndFirstNameApostrAndLastNameMinus() {
        return getBuild("admin", NEW_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1));
    }

    public static UserDto createUserDtoForUpdatingWithChangedUsername() {
        return getBuild(NEW_USERNAME, "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1));
    }

    public static UserDto createUserDtoForUpdatingWithChangedLastOnlineTime() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", "empty", NEW_LAST_ONLINE_TIME);
    }

    public static UserDto createUserDtoForUpdatingWithInvalidShortFirstName() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_SHORT_NAME,
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1));
    }

    public static UserDto createUserDtoForUpdatingWithInvalidLastName() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_TWO_WORDS_NAME,
                "admin", "empty", LocalDateTime.of(2019, 1, 1, 0, 0, 1));
    }

    public static UserDto getBuild(String username, String email, Boolean online, String firstName,
                                   String lastName, String avatarImage, LocalDateTime lastOnlineTime) {
        return UserDto.builder()
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
