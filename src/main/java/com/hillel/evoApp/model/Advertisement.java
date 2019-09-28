package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "advertisements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement extends BaseEntity {

    @Column(name = "topic")
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type")
    private DealType dealType;

    @Column(name = "is_favourite")
    private Boolean isFavourite;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "advertisement_locations",
            joinColumns = {@JoinColumn(name = "advertisement_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "location_id", referencedColumnName = "id")})
    private List<Location> locations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private List<AdvertisementImage> advertisementImages;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private List<ExchangeProduct> exchangeProducts;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Product product;

    @ManyToMany(mappedBy = "advertisements", fetch = FetchType.LAZY)
    private List<User> users;
}
