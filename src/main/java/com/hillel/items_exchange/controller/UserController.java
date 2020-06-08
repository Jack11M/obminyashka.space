package com.hillel.items_exchange.controller;

import java.security.Principal;
import java.util.Locale;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.Valid;

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
import com.hillel.items_exchange.util.MessageSourceUtil;

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

    @PutMapping("/info/{id}")
    public ResponseEntity<UserDto> updateUserInfo(@Valid @RequestBody UserDto userDto,
                                                  @PositiveOrZero @PathVariable("id") long id,
                                                  Principal principal) {
        //TODO get user Id instead of nameOrEmail
        UserDto user = this.getUserDto(principal.getName());
        validateInfoOwner(user.getId(), id);
        return new ResponseEntity<>(userService.update(userDto), HttpStatus.OK);
    }

    private UserDto getUserDto(String nameOrEmail) {
        return userService.getByUsernameOrEmail(nameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSourceUtil.getExceptionMessageSourceWithAdditionalInfo(
                                "user.not-found", nameOrEmail)
                ));
    }

    private void validateInfoOwner(long loginUserId, long userId) {
        if (loginUserId != userId)
            throw new SecurityException(
                    messageSourceUtil.getExceptionMessageSourceWithId(userId, "user.not.allowed"));
    }
}
