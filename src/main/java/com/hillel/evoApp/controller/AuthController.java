package com.hillel.evoApp.controller;

import com.hillel.evoApp.dao.RoleRepository;
import com.hillel.evoApp.dto.UserLoginDto;
import com.hillel.evoApp.dto.UserRegistrationDto;
import com.hillel.evoApp.exception.ResourceNotFoundException;
import com.hillel.evoApp.model.User;
import com.hillel.evoApp.security.jwt.JwtTokenProvider;
import com.hillel.evoApp.service.UserService;
import com.hillel.evoApp.validator.UserLoginDtoValidator;
import com.hillel.evoApp.validator.UserRegistrationDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
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
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto, BindingResult bindingResult) {
        userLoginDtoValidator.validate(userLoginDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResourceNotFoundException(bindingResult.toString());
        }

        try {
            String username = userLoginDto.getUsernameOrEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userLoginDto.getPassword()));
            User user = userService.findByUsernameOrEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

            String token = jwtTokenProvider.createToken(username, user.getRole());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult) {
        userRegistrationDtoValidator.validate(userRegistrationDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResourceNotFoundException(bindingResult.toString());
        }

        userService.registerNewUser(userRegistrationDto, passwordEncoder, roleRepository);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(userRegistrationDto.getUsername()).toUri();

        return ResponseEntity.created(location).body("user registered");
    }
}
