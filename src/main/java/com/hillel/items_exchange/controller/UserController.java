package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<UserDto> getUserDto(@PathVariable("id") Long id, Principal principal) {

        User user = userService.findByUsernameOrEmail(principal.getName()).get();

        if (user != null && user.getId() != id){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return userService.getUserDtoById(id)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
