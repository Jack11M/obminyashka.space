package com.hillel.items_exchange.model;

import java.util.Collections;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "category", "products"})
public class Subcategory {

    public Subcategory(long id, String name) {
        this.id = id;
        this.name = name;
        this.products = Collections.emptyList();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "subcategory")
    private List<Product> products;
}
