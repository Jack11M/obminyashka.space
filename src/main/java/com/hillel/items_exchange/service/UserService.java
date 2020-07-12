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
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.mapper.UtilMapper.convertToDto;
import static com.hillel.items_exchange.mapper.UtilMapper.convertToModel;

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

    public Optional<UserDto> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail).map(this::mapUserToDto);
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public List<ChildDto> getChildren(User parent) {
        return convertToDto(parent.getChildren(), ChildDto.class);
    }

    public void addChildren(User parent, List<ChildDto> childrenDtoToAddList) {
        final List<Child> childrenToSaveList = new ArrayList<>(convertToModel(childrenDtoToAddList, Child.class, ArrayList::new));
        childrenToSaveList.forEach(child -> child.setUser(parent));
        childRepository.saveAll(childrenToSaveList);
    }

    public void updateChildren(User parent, List<ChildDto> childrenDtoToUpdateList) {
        final List<Child> childrenToUpdate = new ArrayList<>(convertToModel(childrenDtoToUpdateList, Child.class, ArrayList::new));
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
