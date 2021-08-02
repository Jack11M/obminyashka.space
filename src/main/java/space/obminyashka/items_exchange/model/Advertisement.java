package space.obminyashka.items_exchange.model;

import java.util.List;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

import org.hibernate.annotations.Type;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"defaultPhoto", "user", "subcategory", "location", "images", "chats"})
public class Advertisement extends BaseEntity {

    private String topic;
    private String description;
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(name = "age")
    private AgeRange age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type")
    private DealType dealType;

    @Column(name = "ready_for_offers")
    private boolean readyForOffers;

    @Column(name = "wishes_to_exchange")
    private String wishesToExchange;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "default_photo")
    private byte[] defaultPhoto;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisement")
    private Set<Chat> chats;

    @PrePersist
    private void addAdvertisementReferences() {
        if(images != null) {
            images.forEach(image -> image.setAdvertisement(this));
        }
    }
}
