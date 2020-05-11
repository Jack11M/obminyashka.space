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
    ResponseEntity<UserDto> getUserInfo(@Validated @PathVariable("id") Long id, Principal principal) {

        UserDto userDto = userService.getByUsernameOrEmail(principal.getName()).orElse(new UserDto());
        if (!id.equals(userDto.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(AccessDeniedException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Exception during request!\nError message:\n" + e.getLocalizedMessage());
    }
}
