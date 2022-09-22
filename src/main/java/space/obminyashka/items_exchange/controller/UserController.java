package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.exception.DataConflictException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.exception.InvalidDtoException;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

import static space.obminyashka.items_exchange.model.enums.Status.DELETED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@RestController
@Api(tags = "User")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    @Value("incorrect.password")
    public String incorrectPassword;
    private static final int MAX_CHILDREN_AMOUNT = 10;

    private final UserService userService;
    private final ImageService imageService;
    private final AdvertisementService advService;

    @GetMapping(ApiKey.USER_MY_INFO)
    @ApiOperation(value = "Find a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<UserDto> getPersonalInfo(@ApiIgnore Authentication authentication) {
        return ResponseEntity.of(userService.findByUsername(authentication.getName()));
    }

    @PutMapping(ApiKey.USER_MY_INFO)
    @ApiOperation(value = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserInfo(@Valid @RequestBody UserUpdateDto userUpdateDto, @ApiIgnore Authentication authentication) {
        return userService.update(userUpdateDto, getUser(authentication.getName()));
    }

    @GetMapping(ApiKey.USER_MY_ADV)
    @ApiOperation(value = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<AdvertisementTitleDto> getCreatedAdvertisements(@ApiIgnore Authentication authentication) {
        return advService.findAllByUsername(authentication.getName());
    }

    @PutMapping(ApiKey.USER_SERVICE_CHANGE_PASSWORD)
    @ApiOperation(value = "Update a user password")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserPassword(@Valid @RequestBody UserChangePasswordDto userChangePasswordDto,
                                     @ApiIgnore Authentication authentication) throws InvalidDtoException {
        User user = findUserByValidCredentials(authentication, userChangePasswordDto.getOldPassword());

        return userService.updateUserPassword(userChangePasswordDto, user);
    }

    @PutMapping(ApiKey.USER_SERVICE_CHANGE_EMAIL)
    @ApiOperation(value = "Update a user email")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 409, message = "CONFLICT")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserEmail(@Valid @RequestBody UserChangeEmailDto userChangeEmailDto, @ApiIgnore Authentication authentication)
            throws DataConflictException {
        User user = getUser(authentication.getName());
        if (user.getEmail().equals(userChangeEmailDto.getNewEmail())) {
            throw new DataConflictException(getMessageSource("exception.email.old"));
        }
        if (userService.existsByEmail(userChangeEmailDto.getNewEmail())) {
            throw new DataConflictException(getMessageSource("email.duplicate"));
        }

        return userService.updateUserEmail(userChangeEmailDto, user);
    }

    @DeleteMapping(ApiKey.USER_SERVICE_DELETE)
    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String selfDeleteRequest(@Valid @RequestBody UserDeleteFlowDto userDeleteFlowDto, @ApiIgnore Authentication authentication)
            throws InvalidDtoException {
        User user = findUserByValidCredentials(authentication, userDeleteFlowDto.getPassword());
        userService.selfDeleteRequest(user);

        return getParametrizedMessageSource("account.self.delete.request", userService.getDaysBeforeDeletion(user));
    }

    @PutMapping(ApiKey.USER_SERVICE_RESTORE)
    @ApiOperation(value = "Restore user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String makeAccountActiveAgain(@Valid @RequestBody UserDeleteFlowDto userDeleteFlowDto, @ApiIgnore Authentication authentication)
            throws InvalidDtoException, IllegalOperationException {
        User user = findUserByValidCredentials(authentication, userDeleteFlowDto.getPassword());
        if (!user.getStatus().equals(DELETED)) {
            throw new IllegalOperationException(getMessageSource("exception.illegal.operation"));
        }
        userService.makeAccountActiveAgain(user);

        return getMessageSource("account.made.active.again");
    }

    @GetMapping(ApiKey.USER_CHILD)
    @ApiOperation(value = "Find a registered requested user's children data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<ChildDto>> getChildren(@ApiIgnore Authentication authentication) {
        List<ChildDto> children = userService.getChildren(getUser(authentication.getName()));
        return children.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(children, HttpStatus.OK);
    }

    @PutMapping(ApiKey.USER_CHILD)
    @ApiOperation(value = "Update child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> updateChildren(@Size(max = MAX_CHILDREN_AMOUNT, message = "{exception.children-amount}")
                                             @RequestBody List<@Valid ChildDto> childrenDto,
                                         @ApiIgnore Authentication authentication) {
        final User user = getUser(authentication.getName());
        return userService.updateChildren(user, childrenDto);
    }

    @PutMapping(value = ApiKey.USER_SERVICE_CHANGE_AVATAR, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Update a user avatar image")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 415, message = "UNSUPPORTED MEDIA TYPE")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserAvatar(@RequestParam MultipartFile image, @ApiIgnore Authentication authentication) {
        User user = getUser(authentication.getName());
        byte[] newAvatarImage = imageService.compress(image);
        userService.setUserAvatar(newAvatarImage, user);
    }

    @DeleteMapping(ApiKey.USER_SERVICE_CHANGE_AVATAR)
    @ApiOperation(value = "Remove a user's avatar image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void removeAvatar(@ApiIgnore Authentication authentication) {
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
                getMessageSource("exception.user.not-found")));
    }
}
