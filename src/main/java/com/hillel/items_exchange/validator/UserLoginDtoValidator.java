package com.hillel.items_exchange.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

import com.hillel.items_exchange.dto.UserLoginDto;
import com.hillel.items_exchange.exception.UnauthorizedException;
import com.hillel.items_exchange.service.UserService;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

@Component
@RequiredArgsConstructor
public class UserLoginDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserLoginDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserLoginDto userLoginDto = (UserLoginDto) o;

        if (!userService.existsByUsernameOrEmailAndPassword(userLoginDto.getUsernameOrEmail(),
                userLoginDto.getPassword())) {

            throw new UnauthorizedException(getExceptionMessageSource("invalid.username-or-password"));
        }
    }
}
