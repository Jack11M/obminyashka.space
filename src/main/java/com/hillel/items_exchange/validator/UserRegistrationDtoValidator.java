package com.hillel.items_exchange.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.UnprocessableEntityException;

@Component
@RequiredArgsConstructor
public class UserRegistrationDtoValidator implements Validator {

    private static final String USERNAME_DUPLICATE = "This login already exists. Please, come up with another login";
    private static final String EMAIL_DUPLICATE = "This email already exists. Please, enter another email or sign in";
    private static final String DIFFERENT_PASSWORDS = "You entered two different passwords. Please try again";
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) o;

        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new UnprocessableEntityException(USERNAME_DUPLICATE);
        }

        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UnprocessableEntityException(EMAIL_DUPLICATE);
        }

        if (!Objects.equals(userRegistrationDto.getConfirmPassword(), userRegistrationDto.getPassword())) {
            throw new BadRequestException(DIFFERENT_PASSWORDS);
        }
    }
}
