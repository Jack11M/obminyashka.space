package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Boolean existsByUsernameOrEmail(String usernameOrEmail);

    Boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String password, BCryptPasswordEncoder bCryptPasswordEncoder);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    Boolean save(User user);

    void registerNewUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder, Role role);
}
