package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encryptedPassword) {
        Pattern emailPattern = Pattern.compile(PatternHandler.EMAIL);
        if (emailPattern.matcher(usernameOrEmail).matches()) {
            return existsByEmailAndPassword(usernameOrEmail, encryptedPassword);
        }
        return existsByUsernameAndPassword(usernameOrEmail, encryptedPassword);
    }

    public boolean registerNewUser(UserRegistrationDto userRegistrationDto, Role role) {
        User registeredUser = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        return userRepository.save(registeredUser).getId() != 0;
    }

    public UserDto update(UserDto newUserDto, User user) {
        BeanUtils.copyProperties(newUserDto, user,
                "id", "created", "updated", "status", "username", "password", "online",
                "lastOnlineTime", "role", "advertisements", "deals", "phones", "children");
        return mapUserToDto(userRepository.saveAndFlush(user));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndPassword(String email, String encodedPassword){
        final Optional<User> user = userRepository.findByEmail(email);
        return user.filter(u -> bCryptPasswordEncoder.matches(encodedPassword, u.getPassword())).isPresent();
    }

    public boolean existsByUsernameAndPassword(String username, String encodedPassword){
        final Optional<User> user = userRepository.findByUsername(username);
        return user.filter(u -> bCryptPasswordEncoder.matches(encodedPassword, u.getPassword())).isPresent();
    }

    public Optional<UserDto> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail).map(this::mapUserToDto);
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
