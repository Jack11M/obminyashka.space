package com.hillel.items_exchange.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "advertisement")
    private Set<Chat> chats;

    @PrePersist
    private void saveProduct() {
        product.setAdvertisement(this);
    }
}
