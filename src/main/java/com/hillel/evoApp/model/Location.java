package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private City city;

    @Column(name = "district")
    private String district;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private List<Advertisement> advertisements;
}
