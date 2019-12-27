package com.hillel.items_exchange.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "advertisement")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String city;
    private String district;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Advertisement> advertisement;
}
