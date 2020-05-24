package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByUsernameOrEmail(String usernameOrEmail) {
        if (userRepository.existsByUsername(usernameOrEmail) || userRepository.existsByEmail(usernameOrEmail)) {
            log.info("IN UserServiceImpl (existsByUsernameOrEmail): user exists by username or email: {}", usernameOrEmail);
            return true;
        } else {
            log.warn("IN UserServiceImpl (existsByUsernameOrEmail): user doesn't exist by username or email: {}", usernameOrEmail);
            return false;
        }
    }

    @Override
    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String password, BCryptPasswordEncoder bCryptPasswordEncoder) {
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
    public boolean save(User user) {
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
    public UserDto update(UserDto newUserDto) {
        User updatedUser = this.mapDtoToUser(newUserDto);
        User currentUser = userRepository.findById(newUserDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (!passwordEncoder.matches(updatedUser.getPassword(), currentUser.getPassword())) {
            throw new IllegalOperationException("Operation is not allowed now");
        }

        BeanUtils.copyProperties(updatedUser, currentUser,
                "id", "advertisements", "deals", "role", "password", "online", "lastOnlineTime",
                "phones", "children", "created", "updated", "status");
        this.intersectList(currentUser.getPhones(), updatedUser.getPhones(), currentUser::addPhone);
        this.intersectList(currentUser.getChildren(), updatedUser.getChildren(), currentUser::addChild);

        return mapUserToDto(userRepository.save(currentUser));
    }

    @Override
    public void registerNewUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder,
                                Role role) {
        User user = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        if (save(user)) {
            log.info("IN UserServiceImpl (registerNewUser): user: {} successfully registered", user);
        } else {
            log.warn("IN UserServiceImpl (registerNewUser): user: {} not registered", user);
        }
    }

    @Override
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

        modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> mapper.skip(User::setRole));
        modelMapper.addConverter(stringLongConverter);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(userDto, User.class);
    }

    private <T> void intersectList(List<T> listToUpdate, List<T> newList, Consumer<T> consumer) {
        listToUpdate.retainAll(newList);
        newList.stream()
                .filter(item -> !listToUpdate.contains(item))
                .forEach(consumer);
    }
}
