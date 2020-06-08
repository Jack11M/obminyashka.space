package com.hillel.items_exchange.util;

import lombok.NoArgsConstructor;

import com.hillel.items_exchange.dto.UserRegistrationDto;

@NoArgsConstructor
public class AuthControllerIntegrationTestUtil {

    protected static final String REGISTER_URL = "/auth/register";
    protected static final String VALID_USERNAME = "test";
    protected static final String VALID_EMAIL = "test@test.com";
    protected static final String VALID_PASSWORD = "Test!1234";
    protected static final String EXISTENT_EMAIL = "admin@gmail.com";
    protected static final String INVALID_PASSWORD = "test123456";
    protected static final String INVALID_EMAIL = "email.com";
    protected static final String INVALID_USERNAME = "user name";

    protected UserRegistrationDto createUserRegistrationDto(String username, String email, String password,
                                                            String confirmPassword) {
        return new UserRegistrationDto(username, email, password, confirmPassword);
    }
}
