package com.hillel.evoApp.dto;

import com.hillel.evoApp.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private List<Location> locations;
    private List<Advertisement> advertisements;
    private List<Deal> deals;
    private List<Phone> phones;
    private List<Child> children;
    private List<UserPhoto> userPhotos;
    private List<ChatRoom> chatRooms;
    private List<Message> messages;
    private Status status;
}
