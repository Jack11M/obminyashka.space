package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

//    2019-11-21: jack.petrov: I need time to figured out correct mapping chats and messages
//@EqualsAndHashCode(callSuper = true, exclude = {"messages", "sender", "receiver"})
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ChatRoom extends BaseEntity {
//
//    private String name;
//
//    @Column(name = "new_message")
//    private Boolean newMessage;
//
//    @ManyToMany(mappedBy = "chatRooms", fetch = FetchType.LAZY)
//    private List<User> users;
//
//    @Column(name = "sender_id")
//    private User sender;
//
//    @Column(name = "receiver_id")
//    private User receiver;
//
//    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Message> messages;
//}
