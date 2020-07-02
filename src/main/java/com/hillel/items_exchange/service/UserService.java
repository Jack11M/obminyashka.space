package com.hillel.items_exchange.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hillel.items_exchange.dao.ChildRepository;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.model.Child;
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

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public List<ChildDto> getChildren(User parent) {
        return parent.getChildren().stream()
                .map(child -> modelMapper.map(child, ChildDto.class))
                .collect(Collectors.toList());
    }

    public void addChildren(User parent, List<ChildDto> childrenDtoToAddList) {
        final List<Child> childrenToSaveList = childrenDtoToAddList.stream()
                .map(dto -> modelMapper.map(dto, Child.class))
                .collect(Collectors.toList());
        childrenToSaveList.forEach(child -> child.setUser(parent));
        childRepository.saveAll(childrenToSaveList);
    }

    public void updateChildren(User parent, List<ChildDto> childrenDtoToUpdateList) {
        final var childrenToUpdate = childrenDtoToUpdateList.stream()
                .map(childDto -> modelMapper.map(childDto, Child.class))
                .collect(Collectors.toList());
        childrenToUpdate.forEach(child -> child.setUser(parent));
        childRepository.saveAll(childrenToUpdate);
        childRepository.flush();
    }

    public void removeChildren(User parent, List<Long> childrenIdToRemoveList) {
        final List<Child> userChildrenToRemoveList = parent.getChildren().stream()
                .filter(child -> childrenIdToRemoveList.contains(child.getId()))
                .collect(Collectors.toList());
        userChildrenToRemoveList.forEach(child -> parent.getChildren().remove(child));
        userRepository.saveAndFlush(parent);
    }
}
