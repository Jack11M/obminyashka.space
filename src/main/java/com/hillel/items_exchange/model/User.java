package com.hillel.items_exchange.model;

import com.hillel.items_exchange.model.enums.Status;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = {"advertisements", "phones", "deals", "children"})
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private Boolean online;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar_image")
    private byte[] avatarImage;

    @Column(name = "last_online_time", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime lastOnlineTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Advertisement> advertisements;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_deal",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "deal_id", referencedColumnName = "id")})
    private List<Deal> deals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Phone> phones;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_chat",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"))
    private Set<Chat> chats;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Message> messages;

    @ManyToMany
    @JoinTable(name = "black_list",
            joinColumns = @JoinColumn(name = "blocker_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id", referencedColumnName = "id"))
    private List<User> blacklistedUsers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole().getName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getStatus() != Status.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus() == Status.ACTIVE;
    }
}
