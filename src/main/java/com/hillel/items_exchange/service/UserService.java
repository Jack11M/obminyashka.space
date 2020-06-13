package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ChildService childService;
    private final PhoneService phoneService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageSourceUtil messageSourceUtil;
    private final ModelMapper modelMapper;

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encryptedPassword) {
        return userRepository.existsByUsernameOrEmailAndPassword(usernameOrEmail, usernameOrEmail, encryptedPassword);
    }

    public boolean registerNewUser(UserRegistrationDto userRegistrationDto, Role role) {
        User registeredUser = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        return userRepository.save(registeredUser).getId() != 0;
    }

    public UserDto update(UserDto newUserDto) {
        User currentUser = userRepository.findById(newUserDto.getId())
                .orElseThrow(IllegalStateException::new);
        User updatedUser = mapDtoToUser(newUserDto);

        if (!bCryptPasswordEncoder.matches(updatedUser.getPassword(), currentUser.getPassword())) {
            throw new IllegalOperationException(
                    messageSourceUtil.getExceptionMessageSource("exception.illegal.password.change"));
        }

        if (!updatedUser.getUsername().equals(currentUser.getUsername())) {
            throw new IllegalOperationException(
                    messageSourceUtil.getExceptionMessageSource("exception.illegal.username.change"));
        }

        BeanUtils.copyProperties(updatedUser, currentUser,
                "id", "advertisements", "deals", "role", "password", "online", "lastOnlineTime",
                "phones", "children", "created", "updated", "status");
        userRepository.flush();
        childService.updateAll(updatedUser.getChildren(), currentUser);
        phoneService.updateAll(updatedUser.getPhones(), currentUser);

        return mapUserToDto(userRepository.save(currentUser));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<UserDto> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail).map(this::mapUserToDto);
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User mapDtoToUser(UserDto userDto) {
        Converter<String, Long> stringLongConverter = new AbstractConverter<>() {
            protected Long convert(String string) {
                return Long.parseLong(string.replaceAll("[^\\d]", ""), 10);
            }
        };
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> mapper.skip(User::setRole));
        modelMapper.addConverter(stringLongConverter);
        return modelMapper.map(userDto, User.class);
    }
}
