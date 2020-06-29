package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.ChildRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final ChildRepository childRepository;

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

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<UserDto> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail).map(this::mapUserToDto);
    }

    public List<ChildDto> getChild(User user) {
        return user.getChildren().stream()
                .map(child -> modelMapper.map(child, ChildDto.class))
                .collect(Collectors.toList());
    }

    public void addChild(User parent, List<ChildDto> childrenList) {
        final var childrenToSave = childrenList.stream()
                .map(dto -> modelMapper.map(dto, Child.class))
                .collect(Collectors.toList());
        childrenToSave.forEach(child -> child.setUser(parent));
        parent.getChildren().addAll(childrenToSave);
        childRepository.saveAll(childrenToSave);
    }

    public void removeChild(User parent, List<Long> childrenIds) {
        final var newChildrenList = parent.getChildren().stream()
                .filter(child -> !childrenIds.contains(child.getId()))
                .collect(Collectors.toList());
        parent.getChildren().clear();
        parent.getChildren().addAll(newChildrenList);
        userRepository.saveAndFlush(parent);
    }

    public void updateChild(User parent, List<ChildDto> childrenList) {
        final var childrenToUpdate = childrenList.stream()
                .map(childDto -> modelMapper.map(childDto, Child.class))
                .collect(Collectors.toList());
        childrenToUpdate.forEach(child -> child.setUser(parent));
        childRepository.saveAll(childrenToUpdate);
        childRepository.flush();
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
