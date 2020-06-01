package com.hillel.items_exchange.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        if (userRepository.existsByUsername(usernameOrEmail)) {
            return userRepository.findByUsername(usernameOrEmail);
        } else if (userRepository.existsByEmail(usernameOrEmail)) {
            return userRepository.findByEmail(usernameOrEmail);
        } else {
            return Optional.empty();
        }
    }

    public boolean registerNewUser(UserRegistrationDto userRegistrationDto,
                                   BCryptPasswordEncoder bCryptPasswordEncoder,
                                   Role role) {

        Optional<User> user = Optional.ofNullable(UserMapper.userRegistrationDtoToUser(userRegistrationDto,
                bCryptPasswordEncoder, role));

        return user.map(existentUser -> {
            userRepository.save(existentUser);
            return true;
        }).orElse(false);
    }

    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail,
                                                      String password,
                                                      BCryptPasswordEncoder bCryptPasswordEncoder) {

        return findByUsernameOrEmail(usernameOrEmail)
                .map(existentUser -> bCryptPasswordEncoder.matches(password, existentUser.getPassword()))
                .orElse(false);
    }
}
