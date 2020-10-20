package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.mapper.ChildMapper;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final ChildMapper childMapper;
    private static final Set<String> READONLY_FIELDS = Set.of("username", "lastOnlineTime", "children", "phones");

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encryptedPassword) {
        Pattern emailPattern = Pattern.compile(PatternHandler.EMAIL);
        Optional<User> user = emailPattern.matcher(usernameOrEmail).matches()
                ? userRepository.findByEmail(usernameOrEmail)
                : userRepository.findByUsername(usernameOrEmail);
        return user.filter(u -> isPasswordMatches(u, encryptedPassword)).isPresent();
    }

    public boolean registerNewUser(UserRegistrationDto userRegistrationDto, Role role) {
        User registeredUser = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        return userRepository.save(registeredUser).getId() != 0;
    }

    public UserDto update(UserDto newUserDto, User user) throws IllegalOperationException {
        checkReadOnlyFieldsUpdate(newUserDto, user);
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

    public Optional<UserDto> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail).map(this::mapUserToDto);
    }

    public boolean isPasswordMatches(User user, String encodedPassword) {
        return bCryptPasswordEncoder.matches(encodedPassword, user.getPassword());
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public List<ChildDto> getChildren(User parent) {

        return parent.getChildren()
                .stream()
                .map(childMapper::convertChild)
                .collect(Collectors.toList());

    }

    public void addChildren(User parent, List<ChildDto> childrenDtoToAdd) {
        final List<Child> childrenToSave = childrenDtoToAdd
                .stream()
                .map(childMapper::convertChildDto)
                .collect(Collectors.toList());
        childrenToSave.forEach(child -> child.setUser(parent));
        parent.getChildren().addAll(childrenToSave);

        userRepository.save(parent);
    }

    public void updateChildren(User parent, List<ChildDto> childrenDtoToUpdate) {
        parent.getChildren().forEach(pChild -> childrenDtoToUpdate.forEach(uChild -> {
            if (pChild.getId() == uChild.getId()) {
                BeanUtils.copyProperties(uChild, pChild);
            }
        }));
        userRepository.saveAndFlush(parent);
    }

    public void removeChildren(User parent, List<Long> childrenIdToRemove) {
        parent.getChildren().removeIf(child -> childrenIdToRemove.contains(child.getId()));
        userRepository.saveAndFlush(parent);
    }

    private void checkReadOnlyFieldsUpdate(UserDto dto, User user) throws IllegalOperationException {
        User convertDto = UserMapper.convertDto(dto);
        String errorResponse = READONLY_FIELDS.stream()
                .filter(fieldName -> !checkReadOnlyFields(convertDto, user, fieldName))
                .collect(Collectors.joining(", "));

        if (!errorResponse.isEmpty()) {
            throw new IllegalOperationException(
                    getExceptionMessageSource("exception.illegal.field.change") + errorResponse);
        }
    }

    @SneakyThrows
    private boolean checkReadOnlyFields(User toCompare, User original, String fieldName) {
        Field declaredField = User.class.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return declaredField.get(toCompare).equals(declaredField.get(original));
    }
}
