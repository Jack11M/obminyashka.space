package space.obminyashka.items_exchange.controller;

import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.RoleNotFoundException;
import space.obminyashka.items_exchange.exception.UserValidationException;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.security.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.validator.UserLoginDtoValidator;
import space.obminyashka.items_exchange.validator.UserRegistrationDtoValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "Authorization")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String AVATARIMAGE = "avatarImage";
    private static final String TOKEN = "token";

    private final UserRegistrationDtoValidator userRegistrationDtoValidator;
    private final UserLoginDtoValidator userLoginDtoValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/login")
    @ApiOperation(value = "Login in a registered user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginDto userLoginDto,
                                                     BindingResult bindingResult) throws UserValidationException {

        userLoginDtoValidator.validate(userLoginDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult.toString());
        }

        try {
            String username = userLoginDto.getUsernameOrEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginDto.getPassword()));
            User user = userService.findByUsernameOrEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            getExceptionMessageSourceWithAdditionalInfo("user.not-found", username)));
            String userFirstName = user.getFirstName();
            String userLastName = user.getLastName();
            String avatarImage = Base64.getEncoder().encodeToString(user.getAvatarImage());
            String token = jwtTokenProvider.createToken(username, user.getRole());
            Map<String, String> response = new HashMap<>();
            response.put(USERNAME, username);
            response.put(FIRSTNAME, userFirstName);
            response.put(LASTNAME, userLastName);
            response.put(AVATARIMAGE, avatarImage);
            response.put(TOKEN, token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(getMessageSource("invalid.username-or-password"));
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "Log out a registered user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest req) {
        final String token = jwtTokenProvider.resolveToken(req);
        jwtTokenProvider.invalidateToken(token);
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 422, message = "UNPROCESSABLE ENTITY")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                   BindingResult bindingResult)
            throws BadRequestException, RoleNotFoundException {

        userRegistrationDtoValidator.validate(userRegistrationDto, bindingResult);

        Role role = roleService.getRole(ROLE_USER).orElseThrow(RoleNotFoundException::new);
        if (userService.registerNewUser(userRegistrationDto, role)) {
            log.info("User with email: {} successfully registered", userRegistrationDto.getEmail());
            return new ResponseEntity<>(getParametrizedMessageSource(
                    "user.created", userRegistrationDto.getUsername()), HttpStatus.CREATED);
        }

        throw new BadRequestException(getMessageSource("user.not-registered"));
    }
}
