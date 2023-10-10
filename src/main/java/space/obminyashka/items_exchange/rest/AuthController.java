package space.obminyashka.items_exchange.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.response.RefreshTokenResponse;
import space.obminyashka.items_exchange.rest.request.UserLoginRequest;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;
import space.obminyashka.items_exchange.rest.request.UserRegistrationRequest;
import space.obminyashka.items_exchange.rest.exception.RefreshTokenException;
import space.obminyashka.items_exchange.rest.exception.bad_request.BadRequestException;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.service.util.EmailType;

import java.util.UUID;

import static liquibase.util.StringUtil.escapeHtml;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@Slf4j
@RestController
@Tag(name = "Authorization")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthService authService;
    private final MailService mailService;

    @PostMapping(value = ApiKey.AUTH_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login in a registered user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest userLoginRequest) {

        try {
            final var username = escapeHtml(userLoginRequest.getUsernameOrEmail());
            var userLoginResponseDto = userService.findAuthDataByUsernameOrEmail(username);
            userLoginResponseDto = authService.finalizeAuthData(userLoginResponseDto);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginResponseDto.getUsername(), userLoginRequest.getPassword()));
            return userLoginResponseDto;
        } catch (AuthenticationException e) {
            log.warn("[AuthController] An exception occurred while authorization for '{}'", userLoginRequest.getUsernameOrEmail(), e);
            throw new BadCredentialsException(getMessageSource(INVALID_USERNAME_PASSWORD));
        }
    }

    @PostMapping(value = ApiKey.AUTH_LOGOUT)
    @Operation(summary = "Log out a registered user")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest req,
                       HttpServletResponse resp,
                       @Parameter(hidden = true) Authentication authentication,
                       @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        new SecurityContextLogoutHandler().logout(req, resp, authentication);
        authService.logout(token, authentication.getName());
    }

    @PostMapping(value = ApiKey.AUTH_REGISTER, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "409", description = "The user is with such email is already registered")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest,
                                               @Parameter(hidden = true) @RequestHeader(HttpHeaders.HOST) String host) throws BadRequestException {
        if (userService.existsByUsernameOrEmail(escapeHtml(userRegistrationRequest.getUsername()), escapeHtml(userRegistrationRequest.getEmail()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(getMessageSource(USERNAME_EMAIL_DUPLICATE));
        }

        UUID codeId = mailService.sendEmailTemplateAndGenerateConfrimationCode(userRegistrationRequest.getEmail(), EmailType.REGISTRATION, host);

        if (userService.registerNewUser(userRegistrationRequest, codeId)) {
            log.info("[AuthController] User with email validation code '{}' is successfully registered", codeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(getMessageSource(USER_CREATED));
        }

        throw new BadRequestException(getMessageSource(USER_NOT_REGISTERED));
    }

    @PostMapping(value = ApiKey.AUTH_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renew access token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Required request header is not present"),
            @ApiResponse(responseCode = "401", description = "Refresh token is expired or not exist")
    })
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponse refreshToken(
            @Parameter(required = true)
            @RequestHeader("refresh") String refreshToken) throws RefreshTokenException {
        final var resolvedToken = JwtTokenService.resolveToken(refreshToken);
        userService.updatePreferableLanguage(resolvedToken);
        return authService.renewAccessTokenByRefresh(resolvedToken);
    }

    @PostMapping(value = ApiKey.AUTH_OAUTH2_SUCCESS, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finish login via OAuth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public UserLoginResponse loginWithOAuth2(@Parameter(hidden = true) Authentication authentication) {
        try {
            var userLoginResponseDto = userService.findAuthDataByUsernameOrEmail(authentication.getName());
            var finalizedAuthData = authService.finalizeAuthData(userLoginResponseDto);
            userService.setValidatedEmailByUsernameOrEmail(authentication.getName());
            return finalizedAuthData;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(getMessageSource(INVALID_OAUTH2_LOGIN));
        }
    }
}
