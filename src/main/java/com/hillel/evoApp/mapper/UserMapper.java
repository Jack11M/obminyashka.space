package com.hillel.evoApp.mapper;

import com.hillel.evoApp.dao.RoleRepository;
import com.hillel.evoApp.dto.UserLoginDto;
import com.hillel.evoApp.dto.UserRegistrationDto;
import com.hillel.evoApp.exception.RoleNotFoundException;
import com.hillel.evoApp.model.Status;
import com.hillel.evoApp.model.User;
import com.hillel.evoApp.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMapper {

    public static User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(roleRepository.findById(1L).orElseThrow(RoleNotFoundException::new));
        Date creationDate = new Date();
        user.setCreated(creationDate);
        user.setUpdated(creationDate);
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public static User userLoginDtoToUser(UserLoginDto userLoginDto, UserService userService) {
        return userService.findByUsernameOrEmail(userLoginDto.getUsernameOrEmail())
                .orElseThrow(() -> new UsernameNotFoundException(("Invalid credential, such email or username doesn't exist")));
    }
}
