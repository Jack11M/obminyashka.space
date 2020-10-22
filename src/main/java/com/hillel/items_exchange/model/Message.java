package com.hillel.items_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Chat chat;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private String text;
    @OneToMany(mappedBy = "message")
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
