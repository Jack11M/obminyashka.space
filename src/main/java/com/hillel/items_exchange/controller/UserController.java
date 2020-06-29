package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final MessageSource messageSource;
    private final MessageSourceUtil messageSourceUtil;
    private final UserService userService;

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
    public ResponseEntity<List<ChildDto>> getChild(Principal principal) {
        return new ResponseEntity<>(userService.getChild(userFromPrincipal(principal)), HttpStatus.OK);
    }

    @PostMapping("/child")
    public ResponseEntity<HttpStatus> addChild(@RequestBody @Size(min = 1, message = "exception.invalid.dto") List<@Valid ChildDto> childrenList,
                                               Principal principal) {
        if (childrenList.stream().anyMatch(dto -> dto.getId() > 0)) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSource("exception.illegal.id.bigger.than.zero"));
        }
        userService.addChild(userFromPrincipal(principal), childrenList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/child")
    public ResponseEntity<HttpStatus> removeChild(@RequestBody @Size(min = 1, message = "{exception.invalid.dto}") List<@NotNull Long> childrenIds,
                                                  Principal principal) {
        final var user = userFromPrincipal(principal);
        final var list = user.getChildren().stream().map(Child::getId).collect(Collectors.toList());
        if (!list.containsAll(childrenIds)) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSource("exception.invalid.dto"));
        }
        userService.removeChild(user, childrenIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/child")
    public ResponseEntity<HttpStatus> updateChild(@RequestBody @Size(min = 1, message = "exception.invalid.dto") List<@Valid ChildDto> childrenList,
                                                  Principal principal) {
        final var user = userFromPrincipal(principal);
        final var listParentChildrenId = user.getChildren().stream().map(Child::getId).collect(Collectors.toList());
        final var listChildrenToUpdateId = childrenList.stream().map(ChildDto::getId).collect(Collectors.toList());
        if (!listParentChildrenId.containsAll(listChildrenToUpdateId)) {
            throw new IllegalIdentifierException(messageSourceUtil.getExceptionMessageSource("exception.invalid.dto"));
        }
        userService.updateChild(user, childrenList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User userFromPrincipal(Principal principal) {
        final var user = userService.findByUsernameOrEmail(principal.getName());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(messageSourceUtil.getExceptionMessageSource("exception.user.not-found"));
        }
        return user.get();
    }
}
