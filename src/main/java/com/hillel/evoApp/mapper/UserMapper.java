package com.hillel.evoApp.mapper;

import com.hillel.evoApp.dao.RoleRepository;
import com.hillel.evoApp.dao.UserRepository;
import com.hillel.evoApp.dto.UserPageDto;
import com.hillel.evoApp.dto.UserLoginDto;
import com.hillel.evoApp.dto.UserRegistrationDto;
import com.hillel.evoApp.exception.RoleNotFoundException;
import com.hillel.evoApp.model.Role;
import com.hillel.evoApp.model.Status;
import com.hillel.evoApp.model.User;
import com.hillel.evoApp.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class UserMapper {

    public UserMapper(BCryptPasswordEncoder cryptPasswordEncoder, UserService userService, RoleRepository roleRepository1) {
    }

    public static UserPageDto userToUserPageDto(User user) {
        UserPageDto userPageDto = new UserPageDto();
        userPageDto.setId(user.getId());
        userPageDto.setUsername(user.getUsername());
        userPageDto.setEmail(user.getEmail());
        userPageDto.setFirstName(user.getFirstName());
        userPageDto.setLastName(user.getLastName());
        userPageDto.setRoles(user.getRoles());
        userPageDto.setLocations(user.getLocations());
        userPageDto.setAdvertisements(user.getAdvertisements());
        userPageDto.setDeals(user.getDeals());
        userPageDto.setPhones(user.getPhones());
        userPageDto.setChildren(user.getChildren());
        userPageDto.setUserPhotos(user.getUserPhotos());
        userPageDto.setChatRooms(user.getChatRooms());
        userPageDto.setMessages(user.getMessages());
        userPageDto.setStatus(user.getStatus());
        return userPageDto;
    }

    public static User userPageDtoToUser(UserPageDto userPageDto, UserService userService) {
        User user = userService.findByUsernameOrEmail(userPageDto.getUsername()).orElse(new User());
        user.setId(userPageDto.getId());
        user.setUsername(userPageDto.getUsername());
        user.setEmail(userPageDto.getEmail());
        user.setFirstName(userPageDto.getFirstName());
        user.setLastName(userPageDto.getLastName());
        user.setRoles(userPageDto.getRoles());
        user.setLocations(userPageDto.getLocations());
        user.setAdvertisements(userPageDto.getAdvertisements());
        user.setDeals(userPageDto.getDeals());
        user.setPhones(userPageDto.getPhones());
        user.setChildren(userPageDto.getChildren());
        user.setUserPhotos(userPageDto.getUserPhotos());
        user.setChatRooms(userPageDto.getChatRooms());
        user.setMessages(userPageDto.getMessages());
        Date updatingDate = new Date();
        user.setUpdated(updatingDate);
        user.setStatus(userPageDto.getStatus());
        return user;
    }

    public static User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        User user = new User();
        Role roleUser = roleRepository.findById(1L).orElseThrow(RoleNotFoundException::new);
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRoles(Collections.singletonList(roleUser));
        Date creationDate = new Date();
        user.setCreated(creationDate);
        user.setUpdated(creationDate);
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public static User userLoginDtoToUser(UserLoginDto userLoginDto, UserService userService) {
        return userService.findByUsernameOrEmail(userLoginDto.getUsernameOrEmail())
                .orElseThrow(() -> new UsernameNotFoundException(("Invalid credential, such email or username doesn't exist")));
    }
}
