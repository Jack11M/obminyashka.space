package space.obminyashka.items_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.controller.request.ChangeEmailRequest;
import space.obminyashka.items_exchange.controller.request.ChangePasswordRequest;
import space.obminyashka.items_exchange.dto.AdvertisementTitleDto;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.dto.UserDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@RestController
@Tag(name = "User")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private static final int MAX_CHILDREN_AMOUNT = 10;

    private final UserService userService;
    private final ImageService imageService;
    private final AdvertisementService advService;

    @GetMapping(value = ApiKey.USER_MY_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<UserDto> getPersonalInfo(@Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.of(userService.findByUsername(authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_MY_INFO, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserInfo(@Valid @RequestBody UserUpdateDto userUpdateDto, @Parameter(hidden = true) Authentication authentication) {
        return userService.update(userUpdateDto, getUser(authentication.getName()));
    }

    @GetMapping(value = ApiKey.USER_MY_ADV, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementTitleDto> getCreatedAdvertisements(@Parameter(hidden = true) Authentication authentication) {
        return advService.findAllByUsername(authentication.getName());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_SERVICE_CHANGE_PASSWORD, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Update a user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "409", description = "CONFLICT")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> updateUserPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                                     @Parameter(hidden = true) Authentication authentication) {
        var username = authentication.getName();
        var password = changePasswordRequest.password();

        if (userService.isUserPasswordMatches(username, password)) {
            return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.ValidationMessage.SAME_PASSWORDS), HttpStatus.CONFLICT);
        }

        userService.updateUserPassword(username, password);

        return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_PASSWORD), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_SERVICE_CHANGE_EMAIL, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Update a user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "409", description = "CONFLICT")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> updateUserEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest,
                                                  @Parameter(hidden = true) Authentication authentication) {
        var username = authentication.getName();
        var email = changeEmailRequest.email();

        if (userService.isUserEmailMatches(username, email)) {
            return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_OLD), HttpStatus.CONFLICT);
        }
        if (userService.existsByEmail(email)) {
            return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.ValidationMessage.DUPLICATE_EMAIL), HttpStatus.CONFLICT);
        }

        userService.updateUserEmail(username, email);

        return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_EMAIL), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(value = ApiKey.USER_SERVICE_DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String selfDeleteRequest(@Parameter(hidden = true) Authentication authentication) {
        userService.selfDeleteRequest(authentication.getName());

        return getParametrizedMessageSource(ResponseMessagesHandler.PositiveMessage.DELETE_ACCOUNT,
                userService.calculateDaysBeforeCompleteRemove(LocalDateTime.now()));
    }

    @PreAuthorize("hasRole('SELF_REMOVING')")
    @PutMapping(value = ApiKey.USER_SERVICE_RESTORE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Restore user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String makeAccountActiveAgain(@Parameter(hidden = true) Authentication authentication) {
        userService.makeAccountActiveAgain(authentication.getName());

        return getMessageSource(ResponseMessagesHandler.PositiveMessage.ACCOUNT_ACTIVE_AGAIN);
    }

    @GetMapping(value = ApiKey.USER_CHILD, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a registered requested user's children data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> getChildren(@Parameter(hidden = true) Authentication authentication) {
        return userService.getChildren(getUser(authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_CHILD, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> updateChildren(@Size(max = MAX_CHILDREN_AMOUNT, message = "{" + ResponseMessagesHandler.ExceptionMessage.CHILDREN_AMOUNT + "}")
                                         @RequestBody List<@Valid ChildDto> childrenDto,
                                         @Parameter(hidden = true) Authentication authentication) {
        final User user = getUser(authentication.getName());
        return userService.updateChildren(user, childrenDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(value = ApiKey.USER_SERVICE_CHANGE_AVATAR, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Set a new user's avatar image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE"),
            @ApiResponse(responseCode = "415", description = "UNSUPPORTED MEDIA TYPE")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, byte[]> updateUserAvatar(@RequestPart MultipartFile image, @Parameter(hidden = true) Authentication authentication) {
        User user = getUser(authentication.getName());
        byte[] newAvatarImage = imageService.scale(image);
        userService.setUserAvatar(newAvatarImage, user);
        return Map.of("avatarImage", newAvatarImage);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(ApiKey.USER_SERVICE_CHANGE_AVATAR)
    @Operation(summary = "Remove a user's avatar image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void removeAvatar(@Parameter(hidden = true) Authentication authentication) {
        userService.removeUserAvatarFor(authentication.getName());
    }

    private User getUser(String username) {
        return userService.findByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                getMessageSource(ResponseMessagesHandler.ExceptionMessage.USER_NOT_FOUND)));
    }
}
