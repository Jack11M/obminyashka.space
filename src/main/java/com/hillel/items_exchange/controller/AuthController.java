package com.hillel.items_exchange.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hillel.items_exchange.dao.RoleRepository;
import com.hillel.items_exchange.dto.UserLoginDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.BadRequestException;
import com.hillel.items_exchange.exception.ResourceNotFoundException;
import com.hillel.items_exchange.exception.RoleNotFoundException;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.security.jwt.JwtTokenProvider;
import com.hillel.items_exchange.service.UserService;
import com.hillel.items_exchange.validator.UserLoginDtoValidator;
import com.hillel.items_exchange.validator.UserRegistrationDtoValidator;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRegistrationDtoValidator userRegistrationDtoValidator;
    private final UserLoginDtoValidator userLoginDtoValidator;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserLoginDto userLoginDto,
                                                     BindingResult bindingResult) {

        userLoginDtoValidator.validate(userLoginDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResourceNotFoundException(bindingResult.toString());
        }

        try {
            String username = userLoginDto.getUsernameOrEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    userLoginDto.getPassword()));

            User user = userService.findByUsernameOrEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

            String token = jwtTokenProvider.createToken(username, user.getRole());
            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto,
                                                BindingResult bindingResult) {

        userRegistrationDtoValidator.validate(userRegistrationDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResourceNotFoundException(bindingResult.toString());
        }

        Role role = roleRepository.findById(1L).orElseThrow(RoleNotFoundException::new);
        if (userService.registerNewUser(userRegistrationDto, passwordEncoder, role)) {
            log.info("User: {} successfully registered", userRegistrationDto);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
        }

        throw new BadRequestException();
    }
}
