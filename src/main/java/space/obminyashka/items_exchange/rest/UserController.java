package space.obminyashka.items_exchange.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.exception.NotImplementedException;
import space.obminyashka.items_exchange.rest.exception.not_found.EntityIdNotFoundException;
import space.obminyashka.items_exchange.rest.request.ChangeEmailRequest;
import space.obminyashka.items_exchange.rest.request.ChangePasswordRequest;
import space.obminyashka.items_exchange.rest.request.MyUserInfoUpdateRequest;
import space.obminyashka.items_exchange.rest.request.VerifyEmailRequest;
import space.obminyashka.items_exchange.rest.response.AdvertisementTitleView;
import space.obminyashka.items_exchange.rest.response.MyUserInfoView;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.MailService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.service.util.EmailType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage.*;

@RestController
@Tag(name = "User")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final AdvertisementService advService;
    private final MailService mailService;

    @GetMapping(value = ApiKey.USER_MY_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")})
    public ResponseEntity<MyUserInfoView> getPersonalInfo(@Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.of(userService.findByUsername(authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_MY_INFO, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserInfo(@Valid @RequestBody MyUserInfoUpdateRequest myUserInfoUpdateRequest, @Parameter(hidden = true) Authentication authentication) {
        return userService.update(myUserInfoUpdateRequest, getUser(authentication.getName()));
    }

    @GetMapping(value = ApiKey.USER_MY_ADV, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user's created advertisements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")})
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementTitleView> getCreatedAdvertisements(@Parameter(hidden = true) Authentication authentication) {
        return advService.findAllByUsername(authentication.getName());
    }

    @GetMapping(value = ApiKey.USER_MY_FAVORITE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user's favorite advertisements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")})
    @ResponseStatus(HttpStatus.OK)
    public Page<AdvertisementTitleView> getFavoriteAdvertisements(
            @Parameter(hidden = true) Authentication authentication,
            @Parameter(name = "page", description = "Results page you want to retrieve (0..N). Default value: 0")
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            @Parameter(name = "size", description = "Number of records per page. Default value: 12")
            @RequestParam(required = false, defaultValue = "12") @Positive int size) {
        return advService.findAllFavorite(authentication.getName(), PageRequest.of(page, size));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(value = ApiKey.USER_MY_FAVORITE_ADV)
    @Operation(summary = "Add user's favorite advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addFavoriteAdvertisement(
            @Parameter(hidden = true) Authentication authentication,
            @Parameter(name = "advertisementId", description = "Id of advertisement for to add an favorite adv")
            @PathVariable UUID advertisementId) {
        if (!advService.existById(advertisementId)) {
            throw new EntityIdNotFoundException(getMessageSource(ADVERTISEMENT_NOT_EXISTED_ID));
        }
        advService.addFavorite(advertisementId, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(value = ApiKey.USER_MY_FAVORITE_ADV)
    @Operation(summary = "Delete user's favorite advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavoriteAdvertisement(
            @Parameter(hidden = true) Authentication authentication,
            @Parameter(name = "advertisementId", description = "Id of advertisement for deleting from favorite adv")
            @PathVariable UUID advertisementId) {
        advService.deleteFavorite(advertisementId, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_SERVICE_CHANGE_PASSWORD, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Update a user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "409", description = "CONFLICT")})
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

    @PostMapping(value = ApiKey.USER_SERVICE_RESET_PASSWORD)
    @Operation(summary = "Reset user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> resetPassword(@Valid @RequestBody VerifyEmailRequest verifyEmailRequest,
                                @Parameter(hidden = true) @RequestHeader(HttpHeaders.HOST) String host) {
        var email = verifyEmailRequest.email();
        var resultMessage = getMessageSource(ResponseMessagesHandler.PositiveMessage.RESET_PASSWORD);
        if (userService.existsByEmail(email)) {
            UUID codeId = mailService.sendEmailTemplateAndGenerateConfrimationCode(email, EmailType.RESET, host);
            userService.saveCodeForResetPassword(email, codeId);
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(resultMessage, HttpStatus.OK);
    }

    @GetMapping(value = ApiKey.USER_SERVICE_PASSWORD_CONFIRM)
    @Operation(summary = "Confirm user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "501", description = "NOT IMPLEMENTED")})
    public void confirmPassword(@PathVariable UUID code) {
        throw new NotImplementedException(getMessageSource(NOT_IMPLEMENTED));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PutMapping(value = ApiKey.USER_SERVICE_CHANGE_EMAIL, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Update a user email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "409", description = "CONFLICT")})
    public ResponseEntity<String> updateUserEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest,
                                                  @Parameter(hidden = true) Authentication authentication,
                                                  @Parameter(hidden = true) @RequestHeader(HttpHeaders.HOST) String host) {
        var username = authentication.getName();
        var email = changeEmailRequest.email();

        if (userService.isUserEmailMatches(username, email)) {
            return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.ExceptionMessage.EMAIL_OLD), HttpStatus.CONFLICT);
        }
        if (userService.existsByEmail(email)) {
            return new ResponseEntity<>(getMessageSource(ResponseMessagesHandler.ValidationMessage.DUPLICATE_EMAIL), HttpStatus.CONFLICT);
        }

        UUID codeId = mailService.sendEmailTemplateAndGenerateConfrimationCode(email, EmailType.CHANGING, host);
        userService.updateUserEmail(username, email, codeId);
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
        byte[] newAvatarImage = imageService.scale(image);
        userService.setUserAvatar(authentication.getName(), newAvatarImage);
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
