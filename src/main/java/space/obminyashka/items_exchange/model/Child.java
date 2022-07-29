package space.obminyashka.items_exchange.model;

import lombok.*;
import org.hibernate.annotations.Type;
import space.obminyashka.items_exchange.model.enums.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "user"})
public class Child {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
