package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.PhoneDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.Phone;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithAdditionalInfo;

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

        if (!currentUser.getUsername().equals(updatedUser.getUsername())) {
            throw new IllegalOperationException(
                    getExceptionMessageSourceWithAdditionalInfo("exception.illegal.field.change", "username"));
        }

        BeanUtils.copyProperties(newUserDto, currentUser,
                "id", "advertisements", "deals", "role", "password", "online", "lastOnlineTime",
                "phones", "children", "created", "updated", "status");
        Set<Child> children = convertToModel(newUserDto.getChildren(), Child.class);
        currentUser.getChildren().addAll(children);
        Set<Phone> phones = newUserDto.getPhones().stream().map(this::mapPhones).collect(Collectors.toSet());
        currentUser.getPhones().removeIf(phone -> phones.stream()
                .map(Phone::getPhoneNumber).noneMatch(phoneNumber -> phoneNumber == phone.getPhoneNumber()));
        currentUser.getPhones().addAll(phones);

        return mapUserToDto(userRepository.saveAndFlush(currentUser));
    }

    private <T, K> Set<K> convertToModel(List<T> tList, Class<K> kClass) {
        return tList.stream().map(t -> modelMapper.map(t, kClass)).collect(Collectors.toSet());
    }

    private Phone mapPhones(PhoneDto phoneDto) {
        Converter<String, Long> stringLongConverter = context ->
                Long.parseLong(context.getSource().replaceAll("[^\\d]", ""));
        modelMapper.typeMap(PhoneDto.class, Phone.class)
                .addMappings(mapper -> mapper.using(stringLongConverter)
                        .map(PhoneDto::getPhoneNumber, Phone::setPhoneNumber));
        return modelMapper.map(phoneDto, Phone.class);
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
