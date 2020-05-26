package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final MessageSource messageSource;
    private final UserService userService;

    @GetMapping("/info/{id}")
    public @ResponseBody
    ResponseEntity<UserDto> getUserInfo(@Validated @PathVariable("id") long id, Principal principal) {

        return ResponseEntity.ok(userService.getByUsernameOrEmail(principal.getName())
                .filter(user -> user.getId() == id)
                .orElseThrow(() -> new AccessDeniedException(messageSource.getMessage(
                        "token.not.equal.to.your.token",
                        null,
                        Locale.getDefault()))
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(AccessDeniedException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Exception during request!\nError message:\n" + e.getLocalizedMessage());
    }
}
