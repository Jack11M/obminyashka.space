package com.hillel.items_exchange.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.MessageSourceUtil;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final MessageSource messageSource;
    private final UserService userService;
    private final MessageSourceUtil messageSourceUtil;

    @GetMapping("/info/{id}")
    public @ResponseBody
    ResponseEntity<UserDto> getUserInfo(@PositiveOrZero @PathVariable("id") long id, Principal principal) {

        return ResponseEntity.ok(userService.getByUsernameOrEmail(principal.getName())
                .filter(user -> user.getId() == id)
                .orElseThrow(() -> new AccessDeniedException(messageSource.getMessage(
                        "exception.access-denied.user-data",
                        null,
                        Locale.getDefault()))
                ));
    }

    @GetMapping("/child")
    public ResponseEntity<List<ChildDto>> getChildren(Principal principal) {
        return new ResponseEntity<>(userService.getChildren(getUser(principal.getName())), HttpStatus.OK);
    }

    @PostMapping("/child")
    public ResponseEntity<HttpStatus> addChildren(@RequestBody @Size(min = 1, message = "exception.invalid.dto") List<@Valid ChildDto> childrenDtoList,
                                                  Principal principal) {
        if (childrenDtoList.stream().anyMatch(dto -> dto.getId() > 0)) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSourceWithAdditionalInfo("exception.illegal.id", "Bigger than zero"));
        }
        userService.addChildren(getUser(principal.getName()), childrenDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/child")
    public ResponseEntity<HttpStatus> removeChildren(@RequestBody @Size(min = 1, message = "{exception.invalid.dto}") List<@NotNull Long> childrenIdToRemoveList,
                                                     Principal principal) {
        final User user = getUser(principal.getName());
        final List<Long> userChildrenIdList = getUserChildrenIdList(user);
        if (!userChildrenIdList.containsAll(childrenIdToRemoveList)) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSource("exception.invalid.dto"));
        }
        userService.removeChildren(user, childrenIdToRemoveList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/child")
    public ResponseEntity<HttpStatus> updateChildren(@RequestBody @Size(min = 1, message = "exception.invalid.dto") List<@Valid ChildDto> childrenDtoList,
                                                     Principal principal) {
        final User user = getUser(principal.getName());
        final List<Long> userChildrenIdList = getUserChildrenIdList(user);
        final List<Long> childrenIdToUpdateList = childrenDtoList.stream().map(ChildDto::getId).collect(Collectors.toList());
        if (!userChildrenIdList.containsAll(childrenIdToUpdateList)) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSource("exception.invalid.dto"));
        }
        userService.updateChildren(user, childrenDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getUser(String username) throws UsernameNotFoundException {
        return userService.findByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException(messageSourceUtil.getExceptionMessageSource("exception.user.not-found")));
    }

    private List<Long> getUserChildrenIdList(User user) {
        return user.getChildren().stream().map(Child::getId).collect(Collectors.toList());
    }
}
