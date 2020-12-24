package com.hillel.items_exchange.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import com.hillel.items_exchange.model.enums.Lang;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    private Lang lang;

    @OneToMany(mappedBy = "location")
    private List<Advertisement> advertisements;
}
