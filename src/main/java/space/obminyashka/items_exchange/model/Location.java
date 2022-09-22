package space.obminyashka.items_exchange.model;

import lombok.*;

import javax.persistence.*;
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
    @Column(name = "area_ua")
    private String areaUA;
    @Column(name = "district_ua")
    private String districtUA;
    @Column(name = "city_ua")
    private String cityUA;
    @Column(name = "area_en")
    private String areaEN;
    @Column(name = "district_en")
    private String districtEN;
    @Column(name = "city_en")
    private String cityEN;

    @OneToMany(mappedBy = "location")
    private List<Advertisement> advertisements;
}
