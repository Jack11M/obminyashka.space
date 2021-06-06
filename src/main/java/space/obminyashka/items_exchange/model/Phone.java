package space.obminyashka.items_exchange.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "user", "defaultPhone"})
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "phone_number")
    private long phoneNumber;
    @Column(name = "default_phone")
    private boolean defaultPhone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
