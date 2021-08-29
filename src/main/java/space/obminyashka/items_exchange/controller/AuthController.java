package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.exception.RoleNotFoundException;
import space.obminyashka.items_exchange.exception.UserValidationException;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.security.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.RefreshTokenService;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.validator.UserLoginDtoValidator;
import space.obminyashka.items_exchange.validator.UserRegistrationDtoValidator;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

import static liquibase.util.StringUtil.escapeHtml;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Authorization")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private static final String ROLE_USER = "ROLE_USER";

    private final UserRegistrationDtoValidator userRegistrationDtoValidator;
    private final UserLoginDtoValidator userLoginDtoValidator;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RoleService roleService;
    private final AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login in a registered user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginDto userLoginDto,
                                      @ApiIgnore BindingResult bindingResult) throws UserValidationException {

        userLoginDtoValidator.validate(userLoginDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult.toString());
        }

        try {
            final var username = escapeHtml(userLoginDto.getUsernameOrEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginDto.getPassword()));
            final var userLoginResponseDto = authService.createUserLoginResponseDto(username);
            log.info("User {} is successfully logged in", username);
            return userLoginResponseDto;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(getMessageSource("invalid.username-or-password"));
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "Log out a registered user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest req) {
        final String accessToken = jwtTokenProvider.resolveAccessToken(req);
        jwtTokenProvider.invalidateAccessToken(accessToken);
        refreshTokenService.deleteByUsername(req.getUserPrincipal().getName());
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 422, message = "UNPROCESSABLE ENTITY")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                               @ApiIgnore BindingResult bindingResult)
            throws BadRequestException, RoleNotFoundException {

        userRegistrationDtoValidator.validate(userRegistrationDto, bindingResult);

        Role role = roleService.getRole(ROLE_USER).orElseThrow(RoleNotFoundException::new);
        if (userService.registerNewUser(userRegistrationDto, role)) {
            log.info("User with email: {} successfully registered", escapeHtml(userRegistrationDto.getEmail()));
            return new ResponseEntity<>(getMessageSource("user.created"), HttpStatus.CREATED);
        }

        throw new BadRequestException(getMessageSource("user.not-registered"));
    }

    @PostMapping(value = "/refresh/token")
    @ApiOperation(value = "Renew access token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED")
    })
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponseDto refreshToken(HttpServletRequest request)
            throws RefreshTokenException {

        final var refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        final var accessToken = refreshTokenService.renewAccessTokenByRefresh(refreshToken)
                .orElseThrow(() -> new RefreshTokenException(getParametrizedMessageSource("refresh.token.invalid",
                        refreshToken)));
        return new RefreshTokenResponseDto(accessToken, refreshToken,
                jwtTokenProvider.getAccessTokenExpiration(LocalDateTime.now()),
                jwtTokenProvider.getRefreshTokenExpiration(LocalDateTime.now()));
    }
}
