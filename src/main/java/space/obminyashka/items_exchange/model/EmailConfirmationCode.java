package space.obminyashka.items_exchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "email_confirmation_token")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EmailConfirmationCode {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime expiryDate;

    @Column(columnDefinition = "DATE", nullable = false)
    @CreatedDate
    private LocalDateTime created;

    public EmailConfirmationCode(UUID id, int expirationHours) {
        this.id = id;
        this.expiryDate = LocalDateTime.now().plusHours(expirationHours);
    }
}
