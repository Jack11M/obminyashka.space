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
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String text;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message", orphanRemoval = true)
    List<Attachment> attachments;
}
