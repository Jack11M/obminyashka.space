package space.obminyashka.items_exchange.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "advertisement"})
public class Image {

    @Id
    @GeneratedValue
    private UUID id;

    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    private byte[] resource;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    public Image(byte[] image, Advertisement adv) {
        this.resource = image;
        this.advertisement = adv;
    }
}
