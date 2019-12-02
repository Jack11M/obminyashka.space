package com.hillel.items_exchange.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"advertisements", "phones", "deals", "children"})
@Entity
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends BaseEntity {

    private String username;
    private String password;
    private String email;

    @Column(name = "on_line")
    private Boolean online;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "avatar_image")
    private String avatarImage;

    @Column(name = "last_online_time")
    private Date lastOnlineTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Advertisement> advertisements;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_deal",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "deal_id", referencedColumnName = "id")})
    private List<Deal> deals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Phone> phones;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Child> children;
}
