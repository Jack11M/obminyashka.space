package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.Status;
import com.hillel.items_exchange.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto,
                                                 BCryptPasswordEncoder bCryptPasswordEncoder,
                                                 Role role) {

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(role);
        user.setFirstName("");
        user.setLastName("");
        user.setOnline(false);
        user.setAvatarImage("");
        user.setAdvertisements(Collections.emptyList());
        user.setChildren(Collections.emptySet());
        user.setDeals(Collections.emptyList());
        user.setPhones(Collections.emptySet());
        LocalDate now = LocalDate.now();
        user.setCreated(now);
        user.setUpdated(now);
        user.setLastOnlineTime(LocalDateTime.now());
        user.setStatus(Status.ACTIVE);
        return user;
    }
}
