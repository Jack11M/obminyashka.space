package com.hillel.items_exchange.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hillel.items_exchange.dao.ChildRepository;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.model.Child;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import org.modelmapper.ModelMapper;

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

    public void addChild(String username, List<ChildDto> childrenList) {
        final var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("");
        }
        if (childrenList.stream().anyMatch(childDto -> childDto.getId() > 0)) {
            throw new IllegalIdentifierException("");
        }
        final var childrenToSave = childrenList.stream()
                .map(childDto -> modelMapper.map(childDto, Child.class))
                .collect(Collectors.toList());
        childrenToSave.forEach(child -> child.setUser(user.get()));
        childRepository.saveAll(childrenToSave);
    }

    public void removeChild(String username, List<ChildDto> childrenList) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("");
        }
        checkReceivedChildData(username, childrenList);
        final var idsToDelete = childrenList.stream().map(ChildDto::getId).collect(Collectors.toList());
        final var newChildrenList = user.get().getChildren().stream()
                .filter(child -> !idsToDelete.contains(child.getId()))
                .collect(Collectors.toList());
        user.get().getChildren().clear();
        user.get().getChildren().addAll(newChildrenList);
        userRepository.saveAndFlush(user.get());
    }

    public void updateChild(String username, List<ChildDto> childrenList) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("");
        }
        checkReceivedChildData(username, childrenList);
        final var childrenToUpdate = childrenList.stream()
                .map(childDto -> modelMapper.map(childDto, Child.class))
                .collect(Collectors.toList());
        childrenToUpdate.forEach(child -> child.setUser(user.get()));
        childRepository.saveAll(childrenToUpdate);
        childRepository.flush();
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private void checkReceivedChildData(String username, List<ChildDto> childrenList) {
        final var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("");
        }
        if (childrenList.stream().map(ChildDto::getId).collect(Collectors.toSet()).size() != childrenList.size()) {
            throw new IllegalIdentifierException("");
        }
        final var userChildrenIds = user.get().getChildren().stream().map(Child::getId).collect(Collectors.toList());
        if (!childrenList.stream().allMatch(childDto -> userChildrenIds.contains(childDto.getId()))) {
            throw new IllegalIdentifierException("");
        }
    }

}
