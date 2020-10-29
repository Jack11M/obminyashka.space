package com.hillel.items_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String text;
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Attachment> attachments;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @PrePersist
    private void onPrePersist() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
        status = MessageStatus.NEW;
    }

    @PreUpdate
    private void onPreUpdate() {
        updated = LocalDateTime.now();
    }
}