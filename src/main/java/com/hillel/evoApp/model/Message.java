package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

//    2019-11-21: jack.petrov: I need time to figured out with correct mapping chats and messages.
//@EqualsAndHashCode(exclude = {"chatRoom", "user"})
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Message{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "content")
//    private String content;
//
//    @Column(name = "created")
//    private Date created;
//
//    private Boolean delivered;
//    private Boolean modified;
//
//    @Column(name = "was_read")
//    private Boolean wasRead;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_id")
//    private ChatRoom chatRoom;
//}
