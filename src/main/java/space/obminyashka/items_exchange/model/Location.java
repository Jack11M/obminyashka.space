package space.obminyashka.items_exchange.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "advertisements"})
public class Location {

    @Id
    @GeneratedValue
    private UUID id;
    private String area;
    private String district;
    private String city;
    private String i18n;

    @OneToMany(mappedBy = "location")
    private List<Advertisement> advertisements;
}
