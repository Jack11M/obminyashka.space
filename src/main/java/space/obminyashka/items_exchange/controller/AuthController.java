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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.dto.UserLoginDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.RefreshTokenException;
import space.obminyashka.items_exchange.exception.RoleNotFoundException;
import space.obminyashka.items_exchange.exception.UserValidationException;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.security.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.security.jwt.refresh.RefreshTokenRequest;
import space.obminyashka.items_exchange.security.jwt.refresh.RefreshTokenResponse;
import space.obminyashka.items_exchange.security.jwt.refresh.RefreshTokenService;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.JwtUtil;
import space.obminyashka.items_exchange.validator.UserLoginDtoValidator;
import space.obminyashka.items_exchange.validator.UserRegistrationDtoValidator;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String EMAIL = "email";
    private static final String AVATAR_IMAGE = "avatarImage";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_EXPIRATION_DATE = "accessTokenExpirationDate";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String REFRESH_TOKEN_EXPIRATION_DATE = "refreshTokenExpirationDate";

    private final UserRegistrationDtoValidator userRegistrationDtoValidator;
    private final UserLoginDtoValidator userLoginDtoValidator;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login in a registered user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> login(@RequestBody @Valid UserLoginDto userLoginDto,
                                     @ApiIgnore BindingResult bindingResult) throws UserValidationException {

        userLoginDtoValidator.validate(userLoginDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserValidationException(bindingResult.toString());
        }

        try {
            final String username = escapeHtml(userLoginDto.getUsernameOrEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginDto.getPassword()));
            final User user = userService.findByUsernameOrEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(getMessageSource("exception.user.not-found")));
            final Map<String, Object> response = new LinkedHashMap<>();
            response.put(USERNAME, username);
            response.put(FIRSTNAME, user.getFirstName());
            response.put(LASTNAME, user.getLastName());
            response.put(EMAIL, user.getEmail());
            response.put(AVATAR_IMAGE, user.getAvatarImage());
            final String accessToken = jwtTokenProvider.createToken(username, user.getRole());
            response.put(ACCESS_TOKEN, accessToken);
            response.put(ACCESS_TOKEN_EXPIRATION_DATE, JwtUtil.getAccessTokenExpiration(LocalDateTime.now()));
            final String refreshToken = refreshTokenService.createRefreshToken(username).getToken();
            response.put(REFRESH_TOKEN, refreshToken);
            response.put(REFRESH_TOKEN_EXPIRATION_DATE, JwtUtil.getRefreshTokenExpiration(LocalDateTime.now()));
            log.info("User {} is successfully logged in", username);

            return response;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(getMessageSource("invalid.username-or-password"));
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "Log out a registered user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest req) {
        final String accessToken = jwtTokenProvider.resolveToken(req);
        jwtTokenProvider.invalidateToken(accessToken);
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
            @ApiResponse(code = 401, message = "UNAUTHORIZED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) throws RefreshTokenException {
        final var token = request.getRefreshToken();
        final var refreshToken = refreshTokenService.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException(getParametrizedMessageSource("refresh.token.not-found", token)));
        if (refreshTokenService.isRefreshTokenExpired(refreshToken)) {
            throw new RefreshTokenException(getParametrizedMessageSource("refresh.token.expired", token));
        }
        final var user = refreshToken.getUser();
        final var accessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        return new RefreshTokenResponse(accessToken, token, JwtUtil.getAccessTokenExpiration(LocalDateTime.now()),
                JwtUtil.getRefreshTokenExpiration(LocalDateTime.now()));
    }
}
