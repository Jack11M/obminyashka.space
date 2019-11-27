package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.Status;
import com.hillel.items_exchange.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMapper {

    public static User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder, Role role) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(role);
        Date creationDate = new Date();
        user.setCreated(creationDate);
        user.setUpdated(creationDate);
        user.setStatus(Status.ACTIVE);
        return user;
    }
}
