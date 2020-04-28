package com.hillel.items_exchange.util;

import lombok.NoArgsConstructor;

import com.hillel.items_exchange.dto.UserRegistrationDto;

@NoArgsConstructor
public class AuthControllerIntegrationTestUtil {

    protected static final String REGISTER_URL = "/auth/register";
    private static final String VALID_USERNAME = "test";
    private static final String VALID_EMAIL = "test@test.com";
    private static final String VALID_PASSWORD = "Test!1234";
    private static final String EXISTENT_EMAIL = "admin@gmail.com";
    private static final String EXISTENT_USERNAME = "admin";
    private static final String INVALID_PASSWORD = "test123456";
    private static final String INVALID_EMAIL = "email.com";
    private static final String INVALID_USERNAME = "user name";

    protected UserRegistrationDto createValidUserRegistrationDto() {
        return new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    }

    protected UserRegistrationDto createUserRegistrationDtoWithExistentEmail() {
        return new UserRegistrationDto(VALID_USERNAME, EXISTENT_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    }

    protected UserRegistrationDto createUserRegistrationDtoWithExistentUsername() {
        return new UserRegistrationDto(EXISTENT_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    }

    protected UserRegistrationDto createUserRegistrationDtoWithDifferentPasswords() {
        return new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD.substring(3));
    }

    protected UserRegistrationDto createUserRegistrationDtoWithInvalidPassword() {
        return new UserRegistrationDto(VALID_USERNAME, VALID_EMAIL, INVALID_PASSWORD, INVALID_PASSWORD);
    }

    protected UserRegistrationDto createUserRegistrationDtoWithInvalidEmail() {
        return new UserRegistrationDto(VALID_USERNAME, INVALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    }

    protected UserRegistrationDto createUserRegistrationDtoWithInvalidUsername() {
        return new UserRegistrationDto(INVALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    }
}
