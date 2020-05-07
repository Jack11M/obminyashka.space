package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.model.BaseEntity;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final MessageSource messageSource;
    private final UserService userService;

    @GetMapping("/info/{id}")
    public @ResponseBody
    ResponseEntity<UserDto> getUserInfo(@PathVariable("id") long id, Principal principal) {

        userService.findByUsernameOrEmail(principal.getName())
                .map(BaseEntity::getId)
                .filter(userId -> userId == id)
                .orElseThrow(() -> new AccessDeniedException(messageSource.getMessage("invalid.token.is.not.equal.to.your.token", null, Locale.getDefault())));

        return userService.getUserDto(id)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(AccessDeniedException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Exception during request!\nError message:\n" + e.getLocalizedMessage());
    }
}
