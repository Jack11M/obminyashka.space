package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.*;
import com.hillel.items_exchange.model.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UserDtoCreatingUtil {

    public static final List<ChildDto> EXISTED_CHILDREN = List.of(
            new ChildDto(1L, Gender.MALE, LocalDate.of(2019, 1, 1)),
            new ChildDto(2L, Gender.FEMALE, LocalDate.of(2019, 2, 2)));
    public static final List<ChildDto> NEW_CHILDREN = List.of(
            new ChildDto(0, Gender.MALE, LocalDate.of(2019, 3, 3)));
    public static final Set<PhoneDto> NEW_PHONES = Set.of(
            new PhoneDto(0, true, "381234567890", true));
    public static final String NEW_USERNAME = "newUsername123";
    public static final String NEW_VALID_EMAIL = "new.admin@gmail.com";
    public static final String NEW_INVALID_DUPLICATE_EMAIL = "test@test.com";
    public static final LocalDateTime NEW_LAST_ONLINE_TIME = LocalDateTime.now();
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "Мар'яна";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "Квітка-Основ'яненко";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";
    public static final byte[] BLANK_AVATAR_IMAGE = "test image png".getBytes();
    public static final String CORRECT_OLD_PASSWORD = "@kuIOIY*h986";
    public static final String WRONG_OLD_PASSWORD = "123456wWWW";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String WRONG_NEW_PASSWORD_CONFIRMATION = "123456qQ";

    public static UserDto createUserDtoForUpdatingWithChangedEmailAndFNameApAndLNameMinusWithoutPhones() {
        return getBuild("admin", NEW_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChangedUsernameWithoutPhones() {
        return getBuild(NEW_USERNAME, "admin@gmail.com", Boolean.FALSE, "super",
                "admin", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChangedLastOnlineTimeWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", BLANK_AVATAR_IMAGE, NEW_LAST_ONLINE_TIME,
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChangedChildrenWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                Collections.emptyList(), Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithChildrenWithoutPhones() {
        return getBuild("test", "test@test.com", Boolean.FALSE, "first",
                "last", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                NEW_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithPhoneWithoutChildren() {
        return getBuild("test", "test@test.com", Boolean.FALSE, "first",
                "last", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                Collections.emptyList(), NEW_PHONES);
    }

    public static UserDto createUserDtoForUpdatingWithNewChildAndPhones() {
        return getBuild("new_user", NEW_VALID_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                NEW_CHILDREN, NEW_PHONES);
    }

    public static UserDto createUserDtoForUpdatingWithPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, "super",
                "admin", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Set.of(new PhoneDto(1L, true, "+381234567890", true)));
    }

    public static UserDto createUserDtoForUpdatingWithInvalidShortFNameWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_SHORT_NAME,
                "admin", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithInvalidLNameWithoutPhones() {
        return getBuild("admin", "admin@gmail.com", Boolean.FALSE, NEW_INVALID_TWO_WORDS_NAME,
                "admin", BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto createUserDtoForUpdatingWithDuplicateEmailWithoutPhones() {
        return getBuild("admin", NEW_INVALID_DUPLICATE_EMAIL, Boolean.FALSE, NEW_VALID_NAME_WITH_APOSTROPHE,
                NEW_VALID_NAME_WITH_HYPHEN_MINUS, BLANK_AVATAR_IMAGE, LocalDateTime.of(2019, 1, 1, 0, 0, 1),
                EXISTED_CHILDREN, Collections.emptySet());
    }

    public static UserDto getBuild(String username, String email, Boolean online, String firstName,
                                   String lastName, byte[] avatarImage, LocalDateTime lastOnlineTime,
                                   List<ChildDto> children, Set<PhoneDto> phones) {
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

    public static UserChangePasswordDto createUserChangePasswordDtoWithCorrectData() {

        return createUserChangePasswordDto(CORRECT_OLD_PASSWORD,
                NEW_PASSWORD,
                NEW_PASSWORD);
    }

    public static UserChangePasswordDto createUserChangePasswordDtoWithWrongOldPassword() {

        return createUserChangePasswordDto(WRONG_OLD_PASSWORD,
                NEW_PASSWORD,
                NEW_PASSWORD);
    }

    public static UserChangePasswordDto createUserChangePasswordDtoWithWrongPasswordConfirmation() {

        return createUserChangePasswordDto(CORRECT_OLD_PASSWORD,
                NEW_PASSWORD,
                WRONG_NEW_PASSWORD_CONFIRMATION);
    }

    public static UserChangeEmailDto createUserChangeEmailDtoWithCorrectData() {

        return createUserChangeEmailDto(NEW_VALID_EMAIL, NEW_VALID_EMAIL);
    }

    public static UserChangeEmailDto createUserChangeEmailDtoWithWrongEmailConfirmation() {

        return createUserChangeEmailDto(NEW_VALID_EMAIL, NEW_INVALID_DUPLICATE_EMAIL);
    }

    public static UserChangePasswordDto createUserChangePasswordDto(String password, String newPassword,
                                                                    String confirmNewPassword) {
        return UserChangePasswordDto.builder()
                .password(password)
                .newPassword(newPassword)
                .confirmNewPassword(confirmNewPassword)
                .build();
    }

    public static UserChangeEmailDto createUserChangeEmailDto(String newEmail, String newEmailConfirmation) {
        return UserChangeEmailDto.builder()
                .newEmail(newEmail)
                .newEmailConfirmation(newEmailConfirmation)
                .build();
    }
}
