package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    @Column(name = "name")
    private String name;

//    2019-09-21: jack.petrov: I need time to figured out why next line does not work.
//    @ManyToMany(mappedBy = "chat_rooms", fetch = FetchType.LAZY)
//    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id")
    private List<Message> messages;
}
