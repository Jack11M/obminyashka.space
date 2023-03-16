package space.obminyashka.items_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import space.obminyashka.items_exchange.controller.request.ChangePasswordRequest;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.exception.DataConflictException;
import space.obminyashka.items_exchange.exception.InvalidDtoException;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.PatternHandler;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@RestController
@Tag(name  = "User")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    @Value(ResponseMessagesHandler.ValidationMessage.INCORRECT_PASSWORD)
    public String incorrectPassword;
    private static final int MAX_CHILDREN_AMOUNT = 10;

    private final UserService userService;
    private final ImageService imageService;
    private final AdvertisementService advService;

    @GetMapping(ApiKey.USER_MY_INFO)
    @Operation(summary = "Find a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<UserDto> getPersonalInfo(@Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.of(userService.findByUsername(authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(ApiKey.USER_MY_INFO)
    @Operation(summary = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserInfo(@Valid @RequestBody UserUpdateDto userUpdateDto, @Parameter(hidden = true) Authentication authentication) {
        return userService.update(userUpdateDto, getUser(authentication.getName()));
    }

    @GetMapping(ApiKey.USER_MY_ADV)
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
    @PutMapping(ApiKey.USER_SERVICE_CHANGE_PASSWORD)
    @Operation(summary = "Update a user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserPassword(@Valid ChangePasswordRequest changePasswordRequest,
                                     @Parameter(hidden = true) Authentication authentication) throws DataConflictException {
        User user = getUser(authentication.getName());
        if (user.getPassword().equals(changePasswordRequest.getPassword())) {
            throw new DataConflictException(getMessageSource(SAME_PASSWORDS));
        }
        user.setPassword(userService.encodeUserPassword(changePasswordRequest.getPassword()));
        userService.update(user);

        return getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_PASSWORD);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(ApiKey.USER_SERVICE_CHANGE_EMAIL)
    @Operation(summary = "Update a user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "409", description = "CONFLICT")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserEmail(@Parameter(name = "New email")
                                  @Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
                                  @RequestParam String email,
                                  @Parameter(hidden = true) Authentication authentication) throws DataConflictException {
        User user = getUser(authentication.getName());
        checkEmailUniqueAndNotUsed(user.getEmail(), email);
        user.setEmail(email);
        userService.update(user);

        return getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_EMAIL);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(ApiKey.USER_SERVICE_DELETE)
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String selfDeleteRequest(@Valid @RequestBody UserDeleteFlowDto userDeleteFlowDto, @Parameter(hidden = true) Authentication authentication)
            throws InvalidDtoException {
        User user = findUserByValidCredentials(authentication, userDeleteFlowDto.getPassword());
        userService.selfDeleteRequest(user);

        return getParametrizedMessageSource(ResponseMessagesHandler.PositiveMessage.DELETE_ACCOUNT,
                userService.calculateDaysBeforeCompleteRemove(user.getUpdated()));
    }

    @PreAuthorize("hasRole('SELF_REMOVING')")
    @PutMapping(ApiKey.USER_SERVICE_RESTORE)
    @Operation(summary = "Restore user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String makeAccountActiveAgain(@Valid @RequestBody UserDeleteFlowDto userDeleteFlowDto, @Parameter(hidden = true) Authentication authentication)
            throws InvalidDtoException {
        User user = findUserByValidCredentials(authentication, userDeleteFlowDto.getPassword());
        userService.makeAccountActiveAgain(user.getUsername());

        return getMessageSource(ResponseMessagesHandler.PositiveMessage.ACCOUNT_ACTIVE_AGAIN);
    }

    @GetMapping(ApiKey.USER_CHILD)
    @Operation(summary = "Find a registered requested user's children data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> getChildren(@Parameter(hidden = true) Authentication authentication) {
        return userService.getChildren(getUser(authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(ApiKey.USER_CHILD)
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
    @PostMapping(value = ApiKey.USER_SERVICE_CHANGE_AVATAR, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    private User findUserByValidCredentials(Authentication authentication, String password) throws InvalidDtoException {
        User user = getUser(authentication.getName());
        if (!userService.isPasswordMatches(user, password)) {
            throw new InvalidDtoException(getMessageSource(incorrectPassword));
        }
        return user;
    }

    private User getUser(String username) {
        return userService.findByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                getMessageSource(ResponseMessagesHandler.ExceptionMessage.USER_NOT_FOUND)));
    }

    private void checkPasswordUniqueAndNotUsed(String userPassword, String newPassword, String confirmPassword)
            throws DataConflictException {
        if (userPassword.equals(newPassword)) {
            throw new DataConflictException(getMessageSource(SAME_PASSWORDS));
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new DataConflictException(getMessageSource(DIFFERENT_PASSWORDS));
        }
    }

    private void checkEmailUniqueAndNotUsed(String currentEmail, String newEmail) throws DataConflictException {
        if (currentEmail.equals(newEmail)) {
            throw new DataConflictException(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_OLD));
        }
        if (userService.existsByEmail(newEmail)) {
            throw new DataConflictException(getMessageSource(
                    ResponseMessagesHandler.ValidationMessage.DUPLICATE_EMAIL));
        }
    }
}
