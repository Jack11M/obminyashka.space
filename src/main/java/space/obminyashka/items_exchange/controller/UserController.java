package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.exception.DataConflictException;
import space.obminyashka.items_exchange.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.exception.InvalidDtoException;
import space.obminyashka.items_exchange.mapper.UtilMapper;
import space.obminyashka.items_exchange.mapper.transfer.New;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.security.Principal;
import java.util.List;

import static space.obminyashka.items_exchange.model.enums.Status.DELETED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.*;

@RestController
@RequestMapping(ApiKey.USER)
@Api(tags = "User")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    @Value("incorrect.password")
    public String incorrectPassword;
    @Value("${max.children.amount}")
    private int maxChildrenAmount;

    private final UserService userService;
    private final ImageService imageService;
    private final AdvertisementService advService;

    @GetMapping("/my-info")
    @ApiOperation(value = "Find a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<UserDto> getPersonalInfo(Principal principal) {
        return ResponseEntity.of(userService.findByUsername(principal.getName()));
    }

    @PutMapping("/my-info")
    @ApiOperation(value = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserInfo(@Valid @RequestBody UserUpdateDto userUpdateDto, @ApiIgnore Principal principal) {
        return userService.update(userUpdateDto, getUser(principal.getName()));
    }

    @GetMapping("/my-adv")
    @ApiOperation(value = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<AdvertisementTitleDto>> getCreatedAdvertisements(@ApiIgnore Principal principal) {
        final var allByUsername = advService.findAllByUsername(principal.getName());
        return allByUsername.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(allByUsername, HttpStatus.OK);
    }

    @PutMapping("/service/pass")
    @ApiOperation(value = "Update a user password")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserPassword(@Valid @RequestBody UserChangePasswordDto userChangePasswordDto,
                                     Principal principal) throws InvalidDtoException {
        User user = getUser(principal.getName());
        if (!userService.isPasswordMatches(user, userChangePasswordDto.getOldPassword())) {
            throw new InvalidDtoException(getMessageSource(incorrectPassword));
        }

        return userService.updateUserPassword(userChangePasswordDto, user);
    }

    @PutMapping("/service/email")
    @ApiOperation(value = "Update a user email")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 409, message = "CONFLICT")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUserEmail(@Valid @RequestBody UserChangeEmailDto userChangeEmailDto, Principal principal)
            throws DataConflictException {
        User user = getUser(principal.getName());
        if (user.getEmail().equals(userChangeEmailDto.getNewEmail())) {
            throw new DataConflictException(getMessageSource("exception.email.old"));
        }
        if (userService.existsByEmail(userChangeEmailDto.getNewEmail())) {
            throw new DataConflictException(getMessageSource("email.duplicate"));
        }

        return userService.updateUserEmail(userChangeEmailDto, user);
    }

    @DeleteMapping("/service/delete")
    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String selfDeleteRequest(@Valid @RequestBody UserDeleteFlowDto userDeleteFlowDto, Principal principal)
            throws InvalidDtoException {
        User user = getUser(principal.getName());
        if (!userService.isPasswordMatches(user, userDeleteFlowDto.getPassword())) {
            throw new InvalidDtoException(getMessageSource(incorrectPassword));
        }
        userService.selfDeleteRequest(user);

        return getParametrizedMessageSource("account.self.delete.request", userService.getDaysBeforeDeletion(user));
    }

    @PutMapping("/service/restore")
    @ApiOperation(value = "Restore user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String makeAccountActiveAgain(@Valid @RequestBody UserDeleteFlowDto userDeleteFlowDto, Principal principal)
            throws InvalidDtoException, IllegalOperationException {
        User user = getUser(principal.getName());
        if (!userService.isPasswordMatches(user, userDeleteFlowDto.getPassword())) {
            throw new InvalidDtoException(getMessageSource(incorrectPassword));
        }
        if (!user.getStatus().equals(DELETED)) {
            throw new IllegalOperationException(getMessageSource("exception.illegal.operation"));
        }
        userService.makeAccountActiveAgain(user);

        return getMessageSource("account.made.active.again");
    }

    @GetMapping("/child")
    @ApiOperation(value = "Find a registered requested user's children data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<ChildDto>> getChildren(Principal principal) {
        List<ChildDto> children = userService.getChildren(getUser(principal.getName()));
        return children.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(children, HttpStatus.OK);
    }

    @PostMapping("/child")
    @ApiOperation(value = "Add children data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 406, message = "NOT_ACCEPTABLE")})
    @ResponseStatus(HttpStatus.OK)
    @Validated({Default.class, New.class})
    public List<ChildDto> addChildren(@RequestBody @Size(min = 1, max = 10, message = "{exception.invalid.dto}")
                                              List<@Valid ChildDto> childrenDto, Principal principal) throws ElementsNumberExceedException {
        User user = getUser(principal.getName());
        int amountOfChildren = childrenDto.size() + user.getChildren().size();
        if (amountOfChildren > maxChildrenAmount) {
            throw new ElementsNumberExceedException(getParametrizedMessageSource(
                    "exception.children-amount", maxChildrenAmount));
        }
        return userService.addChildren(user, childrenDto);
    }

    @DeleteMapping("/child/{id}")
    @ApiOperation(value = "Delete child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeChildren(@PathVariable("id") @Size(min = 1, message = "{exception.invalid.dto}")
                                       List<@NotNull Long> childrenIdToRemove, Principal principal) {
        final User user = getUser(principal.getName());
        if (isNotAllIdPresent(user, childrenIdToRemove)) {
            throw new IllegalIdentifierException(
                    getMessageSource("exception.invalid.dto"));
        }
        userService.removeChildren(user, childrenIdToRemove);
    }

    @PutMapping("/child")
    @ApiOperation(value = "Update child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public List<ChildDto> updateChildren(@RequestBody @Size(min = 1, message = "{exception.invalid.dto}")
                                                 List<@Valid ChildDto> childrenDto, Principal principal) {
        final User user = getUser(principal.getName());
        if (isNotAllIdPresent(user, UtilMapper.mapBy(childrenDto, ChildDto::getId))) {
            throw new IllegalIdentifierException(
                    getExceptionMessageSourceWithAdditionalInfo(
                            "exception.invalid.dto",
                            "Not all children from dto present in User"));
        }
        return userService.updateChildren(user, childrenDto);
    }

    @PostMapping("/service/avatar")
    @ApiOperation(value = "Update a user avatar image")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 415, message = "UNSUPPORTED MEDIA TYPE")})
    @ResponseStatus(HttpStatus.OK)
    public void updateUserAvatar(@RequestParam(value = "file") MultipartFile image, Principal principal) {
        User user = getUser(principal.getName());
        byte[] newAvatarImage = imageService.compress(image);
        userService.setUserAvatar(newAvatarImage, user);
    }

    private boolean isNotAllIdPresent(User parent, List<Long> childrenId) {
        final List<Long> userChildrenId = UtilMapper.mapBy(parent.getChildren(), Child::getId);
        return !userChildrenId.containsAll(childrenId);
    }

    private User getUser(String username) {
        return userService.findByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                getMessageSource("exception.user.not-found")));
    }
}
