package space.obminyashka.items_exchange.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "user", "defaultPhone"})
public class Phone {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    @Column(name = "phone_number")
    private long phoneNumber;
    @Column(name = "default_phone")
    private boolean defaultPhone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
