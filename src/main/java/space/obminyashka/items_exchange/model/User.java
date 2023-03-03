package space.obminyashka.items_exchange.model;

import space.obminyashka.items_exchange.model.enums.Status;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"username", "email", "firstName", "lastName"})
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private Boolean online;

    @Column(name = "oauth2_login")
    private Boolean oauth2login;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar_image")
    private byte[] avatarImage;

    @Column(name = "last_online_time", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime lastOnlineTime;

    private Locale language;

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

    @ManyToMany
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

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

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
        return this.getStatus() != Status.DELETED;
    }
}
