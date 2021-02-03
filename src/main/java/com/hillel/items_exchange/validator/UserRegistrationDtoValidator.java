package com.hillel.items_exchange.validator;

import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.Objects;

import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;

@Component
@RequiredArgsConstructor
public class UserRegistrationDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @SneakyThrows({BadRequestException.class, UnprocessableEntityException.class})
    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) o;

        if (userService.existsByUsername(userRegistrationDto.getUsername())) {
            throw new UnprocessableEntityException(getMessageSource("username.duplicate"));
        }

        if (userService.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UnprocessableEntityException(getMessageSource("email.duplicate"));
        }

        if (!Objects.equals(userRegistrationDto.getConfirmPassword(), userRegistrationDto.getPassword())) {
            throw new BadRequestException(getMessageSource("different.passwords"));
        }

        final FieldError emailError = errors.getFieldError("email");
        if (emailError!=null){
            throw new BadRequestException(emailError.getDefaultMessage());
        }
    }
}
