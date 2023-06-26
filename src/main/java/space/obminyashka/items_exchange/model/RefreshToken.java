package space.obminyashka.items_exchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    @Accessors(chain = true)
    private String token;

    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    @Accessors(chain = true)
    private LocalDateTime expiryDate;

    @Column(name = "created", columnDefinition = "DATE", nullable = false)
    @CreatedDate
    @Accessors(chain = true)
    private LocalDateTime created;

    public RefreshToken(String token, LocalDateTime expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
