package space.obminyashka.items_exchange.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "advertisement"})
public class Image {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] resource;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    public Image(byte[] image, Advertisement adv) {
        this.resource = image;
        this.advertisement = adv;
    }
}
