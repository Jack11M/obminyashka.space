package space.obminyashka.items_exchange.validator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.DataConflictException;
import space.obminyashka.items_exchange.service.UserService;

import static liquibase.util.StringUtil.escapeHtml;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Component
@RequiredArgsConstructor
public class UserRegistrationDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @SneakyThrows({BadRequestException.class, DataConflictException.class})
    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) o;

        if (userService.existsByUsername(escapeHtml(userRegistrationDto.getUsername()))) {
            throw new DataConflictException(getMessageSource("username.duplicate"));
        }

        if (userService.existsByEmail(escapeHtml(userRegistrationDto.getEmail()))) {
            throw new DataConflictException(getMessageSource("email.duplicate"));
        }

        final FieldError emailError = errors.getFieldError("email");
        if (emailError!=null){
            throw new BadRequestException(emailError.getDefaultMessage());
        }
    }
}
