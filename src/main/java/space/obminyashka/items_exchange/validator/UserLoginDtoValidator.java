package space.obminyashka.items_exchange.validator;

import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.exception.UnauthorizedException;
import space.obminyashka.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Component
@RequiredArgsConstructor
public class UserLoginDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserLoginDtoValidator.class.equals(aClass);
    }

    @SneakyThrows(UnauthorizedException.class)
    @Override
    public void validate(Object o, Errors errors) {
        UserLoginDto userLoginDto = (UserLoginDto) o;

        if (!userService.existsByUsernameOrEmailAndPassword(userLoginDto.getUsernameOrEmail(),
                userLoginDto.getPassword())) {

            throw new UnauthorizedException(getMessageSource("invalid.username-or-password"));
        }
    }
}
