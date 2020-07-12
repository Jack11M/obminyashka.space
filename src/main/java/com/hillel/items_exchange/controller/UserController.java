package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithAdditionalInfo;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;
    private final MessageSourceUtil messageSourceUtil;

    @GetMapping("/info/{id}")
    public @ResponseBody
    ResponseEntity<UserDto> getUserInfo(@PositiveOrZero @PathVariable("id") long id, Principal principal) {

        return ResponseEntity.ok(userService.getByUsernameOrEmail(principal.getName())
                .filter(user -> user.getId() == id)
                .orElseThrow(() -> new AccessDeniedException(
                        getExceptionMessageSource("exception.access-denied.user-data"))));
    }

    @PutMapping("/info")
    public ResponseEntity<UserDto> updateUserInfo(@Valid @RequestBody UserDto userDto, Principal principal)
            throws IllegalOperationException {
        User user = userService.findByUsernameOrEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);
        if (user.getId() != userDto.getId()) {
            throw new AccessDeniedException(getExceptionMessageSource("exception.permission-denied.user-profile"));
        }
        if (!userDto.getUsername().equals(user.getUsername())) {
            throw new IllegalOperationException(
                    getExceptionMessageSourceWithAdditionalInfo("exception.illegal.field.change", "username"));
        }
        return new ResponseEntity<>(userService.update(userDto, user), HttpStatus.ACCEPTED);
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

    private <T, E> List<E> mapBy(Collection<T> collection, Function<T, E> mapper) {
        return collection.stream().map(mapper)
                .collect(Collectors.toList());
    }
}
