package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.*;
import lombok.*;
import space.obminyashka.items_exchange.repository.enums.Gender;

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
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
