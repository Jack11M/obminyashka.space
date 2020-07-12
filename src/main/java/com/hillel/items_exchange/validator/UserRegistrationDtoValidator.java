package com.hillel.items_exchange.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.service.UserService;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

@Component
@RequiredArgsConstructor
public class UserRegistrationDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) o;

        if (userService.existsByUsername(userRegistrationDto.getUsername())) {
            throw new UnprocessableEntityException(getExceptionMessageSource("username.duplicate"));
        }

        if (userService.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UnprocessableEntityException(getExceptionMessageSource("email.duplicate"));
        }

        if (!Objects.equals(userRegistrationDto.getConfirmPassword(), userRegistrationDto.getPassword())) {
            throw new BadRequestException(getExceptionMessageSource("different.passwords"));
        }
    }
}
