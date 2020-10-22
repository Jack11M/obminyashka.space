package com.hillel.items_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hash", nullable = false, unique = true)
    private String hash;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;
    @ManyToMany(mappedBy = "chats")
    private List<User> users;
    @OneToMany(mappedBy = "chat")
    private List<Message> messages;
}
