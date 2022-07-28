package space.obminyashka.items_exchange.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import space.obminyashka.items_exchange.model.enums.Gender;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "user"})
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", insertable = false, updatable = false, nullable = false)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
