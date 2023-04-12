package space.obminyashka.items_exchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "email_confirmation_code")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EmailConfirmationCode {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime expiryDate;

    @Column(columnDefinition = "DATE", nullable = false)
    @CreatedDate
    private LocalDateTime created;

    public EmailConfirmationCode(User user, UUID id, LocalDateTime expiryDate) {
        this.user = user;
        this.id = id;
        this.expiryDate = expiryDate;
    }
}
