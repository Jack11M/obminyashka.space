package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    boolean existsByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String password, BCryptPasswordEncoder bCryptPasswordEncoder);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    boolean save(User user);

    void registerNewUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder, Role role);
}
