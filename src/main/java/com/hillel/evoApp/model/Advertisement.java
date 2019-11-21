package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true, exclude = {"user", "product", "locations"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement extends BaseEntity {

    private String topic;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type")
    private DealType dealType;

    @Column(name = "is_favourite")
    private Boolean isFavourite;

    @Column(name = "ready_for_offers")
    private Boolean readyForOffers;

    @OneToOne(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location locations;

    @Column(name = "wishes_to_exchange")
    private String wishesToExchange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Product product;
}
