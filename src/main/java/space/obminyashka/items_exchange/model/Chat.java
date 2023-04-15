package space.obminyashka.items_exchange.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "hash")
public class Chat {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "hash", nullable = false, unique = true)
    private String hash;
    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;
    @ManyToMany(mappedBy = "chats")
    private Set<User> users;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Chat(String chatHash, Advertisement advertisement, Set<User> users, List<Message> messages) {
        this.hash = chatHash;
        this.advertisement = advertisement;
        this.users = users;
        this.messages = messages;
    }
}
