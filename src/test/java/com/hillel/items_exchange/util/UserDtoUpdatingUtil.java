package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.PhoneDto;
import com.hillel.items_exchange.dto.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDtoUpdatingUtil {

    private static final String VALID_PASSWORD = "@kuIOIY*h986";

    private static final String VALID_PHONE_NUMBER = "38(123)456-78-90";
    private static final String INVALID_PHONE_NUMBER = "38(123)456-78-90ABC";

    private static final LocalDate VALID_CHILD_BIRTH_DATE = LocalDate.of(2010, 1, 1);
    private static final LocalDate INVALID_CHILD_BIRTH_DATE = LocalDate.of(2030, 1, 1);

    public static UserDto getValidUserDto() {
        return createUserDto(VALID_PASSWORD, VALID_PHONE_NUMBER, VALID_CHILD_BIRTH_DATE);
    }

    public static UserDto getInvalidUserDtoWithInvalidPhoneNumber() {
        return createUserDto(VALID_PASSWORD, INVALID_PHONE_NUMBER, VALID_CHILD_BIRTH_DATE);
    }

    public static UserDto getInvalidUserDtoWithInvalidChildBirthDate() {
        return createUserDto(VALID_PASSWORD, INVALID_PHONE_NUMBER, INVALID_CHILD_BIRTH_DATE);
    }

    private static UserDto createUserDto(String password, String phoneNumber, LocalDate childBirthDate) {
        List<ChildDto> children = new ArrayList<>(List.of(new ChildDto(1L, "male", childBirthDate)));
        List<PhoneDto> phones = new ArrayList<>(List.of(new PhoneDto(1L, Boolean.TRUE, phoneNumber, Boolean.TRUE)));

        return new UserDto(1L, "admin", password, "admin@gmail.com", Boolean.FALSE,
                "super", "admin", "empty",
                LocalDate.of(2019, 1, 1), phones, children);
    }
}
