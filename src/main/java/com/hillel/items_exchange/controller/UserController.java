package com.hillel.items_exchange.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.PositiveOrZero;

import com.hillel.items_exchange.dto.ChildDto;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @PostMapping("/child")
    public ResponseEntity<HttpStatus> addChild(@RequestBody List<ChildDto> childrenList,
                                               Principal principal){
        userService.addChild(principal.getName(), childrenList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/child")
    public ResponseEntity<HttpStatus> removeChild(@RequestBody List<ChildDto> childrenList,
                                               Principal principal){
        userService.removeChild(principal.getName(), childrenList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/child")
    public ResponseEntity<HttpStatus> updateChild(@RequestBody List<ChildDto> childrenList,
                                                  Principal principal){
        userService.updateChild(principal.getName(), childrenList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
