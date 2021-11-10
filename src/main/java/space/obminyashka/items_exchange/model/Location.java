package space.obminyashka.items_exchange.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "advertisements"})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String area;
    private String district;
    private String city;

    @JsonAlias("lang")
    private String i18n;

    @OneToMany(mappedBy = "location")
    private List<Advertisement> advertisements;
}
