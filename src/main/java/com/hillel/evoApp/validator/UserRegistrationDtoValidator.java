package com.hillel.evoApp.validator;

import com.hillel.evoApp.dao.UserRepository;
import com.hillel.evoApp.dto.UserRegistrationDto;
import com.hillel.evoApp.exception.UnauthorizedException;
import com.hillel.evoApp.exception.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserRegistrationDtoValidator implements Validator {

    private final UserRepository userRepository;

    @Value("${empty.username.field}")
    private String emptyUsernameField;

    @Value("${invalid.username.size}")
    private String invalidUsernameSize;

    @Value("${invalid.username}")
    private String invalidUsername;

    @Value("${username.duplicate}")
    private String usernameDuplicate;

    @Value("${too.big.email}")
    private String tooBigEmail;

    @Value("${email.duplicate}")
    private String emailDuplicate;

    @Value("${invalid.email}")
    private String invalidEmail;

    @Value("${empty.email.field}")
    private String emptyEmailField;

    @Value("${empty.password.field}")
    private String emptyPasswordField;

    @Value("${invalid.password.size}")
    private String invalidPasswordSize;

    @Value("${empty.confirm.password.field}")
    private String emptyConfirmPasswordField;

    @Value("${different.passwords}")
    private String differentPasswords;

    @Value("${valid.email.reg.ex}")
    private String validEmailRegEx;

    @Value("${valid.password.reg.ex}")
    private String validPasswordRegEx;

    @Value("${valid.username.reg.ex}")
    private String validUsernameRegEx;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) o;

        if (userRegistrationDto.getUsername().isEmpty()) {
            throw new UnauthorizedException(emptyUsernameField);
        }

        if (userRegistrationDto.getUsername().length() < 2 || userRegistrationDto.getUsername().length() > 50) {
            throw new UnauthorizedException(invalidUsernameSize);
        }

        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new UnprocessableEntityException(usernameDuplicate);
        }

        if (regExValidator(validUsernameRegEx, userRegistrationDto.getUsername())) {
            throw new UnauthorizedException(invalidUsername);
        }

        if (userRegistrationDto.getEmail().length() > 130) {
            throw new UnauthorizedException(tooBigEmail);
        }

        if (userRegistrationDto.getEmail().isEmpty()) {
            throw new UnauthorizedException(emptyEmailField);
        }

        if (regExValidator(validEmailRegEx, userRegistrationDto.getEmail())) {
            throw new UnauthorizedException(invalidEmail);
        }

        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UnprocessableEntityException(emailDuplicate);
        }

        if (userRegistrationDto.getPassword().isEmpty()) {
            throw new UnauthorizedException(emptyPasswordField);
        }

        if (userRegistrationDto.getPassword().length() < 8 || userRegistrationDto.getPassword().length() > 30) {
            throw new UnauthorizedException(invalidPasswordSize);
        }

        if (userRegistrationDto.getConfirmPassword().isEmpty()) {
            throw new UnauthorizedException(emptyConfirmPasswordField);
        }

        if (regExValidator(validPasswordRegEx, userRegistrationDto.getPassword())) {
            throw new UnauthorizedException(invalidPasswordSize);
        }

        if (!userRegistrationDto.getConfirmPassword().equals(userRegistrationDto.getPassword())) {
            throw new UnauthorizedException(differentPasswords);
        }
    }

    private boolean regExValidator(String requiredPattern, String stringForCheck) {
        Pattern p = Pattern.compile(requiredPattern);
        Matcher m = p.matcher(stringForCheck);
        return !m.matches();
    }
}
