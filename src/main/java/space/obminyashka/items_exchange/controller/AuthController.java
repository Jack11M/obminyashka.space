package space.obminyashka.items_exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.RefreshTokenResponseDto;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.bad_request.BadRequestException;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.service.AuthService;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.EmailType;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.UUID;

import static liquibase.util.StringUtil.escapeHtml;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

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
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginDto userLoginDto) {

        try {
            final var username = escapeHtml(userLoginDto.getUsernameOrEmail());
            var userLoginResponseDto = userService.findLoginResponseDtoByUsernameOrEmail(username);
            userLoginResponseDto = authService.finalizeUserLoginResponseDto(userLoginResponseDto);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginResponseDto.getUsername(), userLoginDto.getPassword()));
            return userLoginResponseDto;
        } catch (AuthenticationException e) {
            log.warn("[AuthController] An exception occurred while authorization for '{}'", userLoginDto.getUsernameOrEmail(), e);
            throw new BadCredentialsException(getMessageSource(
                    ResponseMessagesHandler.ValidationMessage.INVALID_USERNAME_PASSWORD));
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
        if (!authService.logout(token, authentication.getName())) {
            String errorMessageTokenNotStartWithBearerPrefix = getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_TOKEN);
            log.error("Unauthorized: {}", errorMessageTokenNotStartWithBearerPrefix);
            req.setAttribute("detailedError", errorMessageTokenNotStartWithBearerPrefix);
        }
    }

    @PostMapping(value = ApiKey.AUTH_REGISTER, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "409", description = "The user is with such email is already registered")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                               @Parameter(hidden = true) @RequestHeader(HttpHeaders.HOST) String host) throws BadRequestException {
        if (userService.existsByUsernameOrEmail(escapeHtml(userRegistrationDto.getUsername()), escapeHtml(userRegistrationDto.getEmail()))) {
            return new ResponseEntity<>(getMessageSource(
                    ResponseMessagesHandler.ValidationMessage.USERNAME_EMAIL_DUPLICATE), HttpStatus.CONFLICT);
        }

        UUID codeId = mailService.sendEmailTemplateAndGenerateConfrimationCode(userRegistrationDto.getEmail(), EmailType.REGISTRATION, host);

        if (userService.registerNewUser(userRegistrationDto, codeId)) {
            log.info("User with email: {} successfully registered", escapeHtml(userRegistrationDto.getEmail()));
            return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.ValidationMessage.USER_CREATED), HttpStatus.CREATED);
        }

        throw new BadRequestException(getMessageSource(
                ResponseMessagesHandler.ValidationMessage.USER_NOT_REGISTERED));
    }

    @PostMapping(value = ApiKey.AUTH_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renew access token with refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Required request header is not present"),
            @ApiResponse(responseCode = "401", description = "Refresh token is expired or not exist")
    })
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponseDto refreshToken(
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
    public UserLoginResponseDto loginWithOAuth2(@Parameter(hidden = true) Authentication authentication) {
        try {
            var userLoginResponseDto = userService.findLoginResponseDtoByUsernameOrEmail(authentication.getName());
            return authService.finalizeUserLoginResponseDto(userLoginResponseDto);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(getMessageSource(
                    ResponseMessagesHandler.ValidationMessage.INVALID_OAUTH2_LOGIN));
        }
    }
}
