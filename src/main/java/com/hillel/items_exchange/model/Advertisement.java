package com.hillel.items_exchange.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"user", "product", "location"})
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

    @Column(name = "wishes_to_exchange")
    private String wishesToExchange;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "advertisement", cascade = CascadeType.ALL)
    private Product product;
}
