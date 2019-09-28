package com.hillel.evoApp.service.impl;

import com.hillel.evoApp.dao.RoleRepository;
import com.hillel.evoApp.dao.UserRepository;
import com.hillel.evoApp.dto.UserRegistrationDto;
import com.hillel.evoApp.mapper.UserMapper;
import com.hillel.evoApp.model.User;
import com.hillel.evoApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public Boolean existsByUsernameOrEmail(String usernameOrEmail) {
        if (userRepository.existsByUsername(usernameOrEmail) || userRepository.existsByEmail(usernameOrEmail)) {
            log.info("IN UserServiceImpl (existsByUsernameOrEmail): user exists by username or email: {}", usernameOrEmail);
            return true;
        } else {
            log.warn("IN UserServiceImpl (existsByUsernameOrEmail): user doesn't exist by username or email: {}", usernameOrEmail);
            return false;
        }
    }

    @Override
    public Boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String password, BCryptPasswordEncoder bCryptPasswordEncoder) {
        if (existsByUsernameOrEmail(usernameOrEmail)) {
            User user = findByUsernameOrEmail(usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("IN UserServiceImpl (existsByUsernameOrEmailAndPassword): " +
                            "User was not found by username or email: " + usernameOrEmail));
            if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
                log.info("IN UserServiceImpl (existsByUsernameOrEmailAndPassword): user exists by password");
                return true;
            } else {
                log.warn("IN UserServiceImpl (existsByUsernameOrEmailAndPassword): user doesn't exist by password: {}", password);
                return false;
            }
        } else {
            log.warn("IN UserServiceImpl (existsByUsernameOrEmailAndPassword): user doesn't exist by (username or email) and password");
            return false;
        }
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        if (userRepository.existsByUsername(usernameOrEmail)) {
            log.info("IN UserServiceImpl (findByUsernameOrEmail): user was found by username: {}", usernameOrEmail);
            return userRepository.findByUsername(usernameOrEmail);
        } else if (userRepository.existsByEmail(usernameOrEmail)) {
            log.info("IN UserServiceImpl (findByUsernameOrEmail): user was found by email: {}", usernameOrEmail);
            return userRepository.findByEmail(usernameOrEmail);
        } else {
            log.warn("IN UserServiceImpl (findByUsernameOrEmail): user was not found by username or email: {}", usernameOrEmail);
            return Optional.empty();
        }
    }

    @Override
    public Boolean save(User user) {
        if (user != null) {
            userRepository.save(user);
            log.info("IN UserServiceImpl (save): user with username: {} successfully saved", user.getUsername());
            return true;
        } else {
            log.warn("IN UserServiceImpl (save): empty user was not saved");
            return false;
        }
    }

    @Override
    public void registerNewUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder,
                                RoleRepository roleRepository) {
        User user = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, roleRepository);
        if (save(user)) {
            log.info("IN UserServiceImpl (registerNewUser): user: {} successfully registered", user);
        } else {
            log.warn("IN UserServiceImpl (registerNewUser): user: {} not registered", user);
        }
    }
}
