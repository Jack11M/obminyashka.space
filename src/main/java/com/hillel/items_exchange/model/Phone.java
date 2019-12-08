package com.hillel.items_exchange.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "user")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @Column(name = "show")
    private Boolean show;

    @Column(name = "default_phone")
    private Boolean defaultPhone;
}
