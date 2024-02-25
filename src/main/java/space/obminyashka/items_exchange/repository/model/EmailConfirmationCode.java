package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "email_confirmation_code")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EmailConfirmationCode {
    @Id
    private UUID id;

    private String type;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime expiryDate;

    @Column(columnDefinition = "DATE", nullable = false)
    @CreatedDate
    private LocalDateTime created;

    public EmailConfirmationCode(UUID id, User user, int expirationHours, String type) {
        this.id = id;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusHours(expirationHours);
        this.type = type;
    }
}
