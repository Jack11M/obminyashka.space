package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.dto.RefreshTokenResponseDto;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.DataConflictException;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.exception.RoleNotFoundException;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static liquibase.util.StringUtil.escapeHtml;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Authorization")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private static final String ROLE_USER = "ROLE_USER";

    private final AuthenticationManager authenticationManager;
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
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {

        try {
            final var username = escapeHtml(userLoginDto.getUsernameOrEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginDto.getPassword()));
            return ResponseEntity.of(authService.createUserLoginResponseDto(username));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(getMessageSource("invalid.username-or-password"));
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "Log out a registered user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest req, HttpServletResponse resp, Authentication auth, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        new SecurityContextLogoutHandler().logout(req, resp, auth);
        if (!authService.logout(token, auth.getName())) {
            String errorMessageTokenNotStartWithBearerPrefix = getMessageSource("invalid.token");
            log.error("Unauthorized: {}", errorMessageTokenNotStartWithBearerPrefix);
            req.setAttribute("detailedError", errorMessageTokenNotStartWithBearerPrefix);
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 422, message = "UNPROCESSABLE ENTITY")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto)
            throws BadRequestException, RoleNotFoundException, DataConflictException {

        if (userService.existsByUsernameOrEmail(escapeHtml(userRegistrationDto.getUsername()), escapeHtml(userRegistrationDto.getEmail()))) {
            throw new DataConflictException(getMessageSource("username-email.duplicate"));
        }

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
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(
            @RequestHeader("refresh_token") String refreshToken,
            @ApiIgnore Authentication authentication) throws RefreshTokenException {
        return ResponseEntity.ok(authService.renewAccessTokenByRefresh(refreshToken, authentication.getName()));
    }
}
