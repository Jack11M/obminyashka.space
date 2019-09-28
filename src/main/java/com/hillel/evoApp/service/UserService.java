package com.hillel.evoApp.service;

import com.hillel.evoApp.dao.RoleRepository;
import com.hillel.evoApp.dto.UserRegistrationDto;
import com.hillel.evoApp.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Boolean existsByUsernameOrEmail(String usernameOrEmail);

    Boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String password, BCryptPasswordEncoder bCryptPasswordEncoder);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    Boolean save(User user);

    void registerNewUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository);
}
