package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.ElementsNumberExceedException;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.mapper.UtilMapper;
import com.hillel.items_exchange.mapper.transfer.New;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.security.Principal;
import java.util.List;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithAdditionalInfo;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionParametrizedMessageSource;

@RestController
@RequestMapping("/user")
@Api(tags = "User")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    @Value("${max.children.amount}")
    private int maxChildrenAmount;

    private final UserService userService;

    @GetMapping("/my-info")
    @ApiOperation(value = "Find a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<UserDto> getPersonalInfo(Principal principal) {
        return ResponseEntity.of(userService.getByUsernameOrEmail(principal.getName()));
    }

    @PutMapping("/info")
    @ApiOperation(value = "Update a registered requested user's data")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto updateUserInfo(@Valid @RequestBody UserDto userDto, Principal principal)
            throws InvalidDtoException, IllegalOperationException {
        User user = getUser(principal.getName());
        if (!user.getEmail().equals(userDto.getEmail()) && userService.existsByEmail(userDto.getEmail())) {
            throw new InvalidDtoException(getExceptionMessageSource("email.duplicate"));
        }
        return userService.update(userDto, user);
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
            @ApiResponse(code = 406, message = "NOT_ACCEPTABLE")})
    @ResponseStatus(HttpStatus.OK)
    @Validated({Default.class, New.class})
    public List<ChildDto> addChildren(@RequestBody @Size(min = 1, max = 10, message = "{exception.invalid.dto}")
                                 List<@Valid ChildDto> childrenDto, Principal principal) throws ElementsNumberExceedException {
        User user = getUser(principal.getName());
        int amountOfChildren = childrenDto.size() + user.getChildren().size();
        if (amountOfChildren > maxChildrenAmount) {
            throw new ElementsNumberExceedException(getExceptionParametrizedMessageSource(
                    "exception.children-amount", maxChildrenAmount));
        }
        return userService.addChildren(user, childrenDto);
    }

    @DeleteMapping("/child/{id}")
    @ApiOperation(value = "Delete child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "NO CONTENT"),
            @ApiResponse(code = 400, message = "BAD REQUEST")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeChildren(@PathVariable("id") @Size(min = 1, message = "{exception.invalid.dto}")
                                           List<@NotNull Long> childrenIdToRemove, Principal principal) {
        final User user = getUser(principal.getName());
        if (isNotAllIdPresent(user, childrenIdToRemove)) {
            throw new IllegalIdentifierException(
                    getExceptionMessageSource("exception.invalid.dto"));
        }
        userService.removeChildren(user, childrenIdToRemove);
    }

    @PutMapping("/child")
    @ApiOperation(value = "Delete child data for a registered requested user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST")})
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

    private boolean isNotAllIdPresent(User parent, List<Long> childrenId) {
        final List<Long> userChildrenId = UtilMapper.mapBy(parent.getChildren(), Child::getId);
        return !userChildrenId.containsAll(childrenId);
    }

    private User getUser(String username) {
        return userService.findByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                getExceptionMessageSource("exception.user.not-found")));
    }
}
