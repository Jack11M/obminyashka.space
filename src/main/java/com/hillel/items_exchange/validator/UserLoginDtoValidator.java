package com.hillel.items_exchange.validator;

import com.hillel.items_exchange.dto.UserLoginDto;
import com.hillel.items_exchange.exception.UnauthorizedException;
import com.hillel.items_exchange.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserLoginDtoValidator implements Validator {

    private static final String INVALID_LOGIN_FORM = "Please enter valid email/login or password";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public UserLoginDtoValidator(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserLoginDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserLoginDto userLoginDto = (UserLoginDto) o;

        if (!userService.existsByUsernameOrEmailAndPassword(userLoginDto.getUsernameOrEmail(), userLoginDto.getPassword(), bCryptPasswordEncoder)) {
            throw new UnauthorizedException(INVALID_LOGIN_FORM);
        }
    }
}
