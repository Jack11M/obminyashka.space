package space.obminyashka.items_exchange.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "advertisement"})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] resource;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;
}
