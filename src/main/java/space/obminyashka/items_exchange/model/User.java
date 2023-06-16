package space.obminyashka.items_exchange.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import space.obminyashka.items_exchange.model.enums.Status;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    @Accessors(chain = true)
    private String username;
    private String password;
    @Column(unique = true)
    @Accessors(chain = true)
    private String email;
    @Builder.Default
    private Boolean online = false;
    @Builder.Default
    private Boolean oauth2Login = false;

    @Accessors(fluent = true)
    @Column(name = "validated_email")
    @Builder.Default
    private Boolean isValidatedEmail = false;

    @Column(name = "first_name")
    @Builder.Default
    private String firstName = "";

    @Column(name = "last_name")
    @Builder.Default
    private String lastName = "";

    @Column(name = "avatar_image")
    @Builder.Default
    private byte[] avatarImage = new byte[0];

    @Column(name = "last_online_time", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime lastOnlineTime;

    @Builder.Default
    private Locale language = LocaleContextHolder.getLocale();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    @Accessors(chain = true)
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
    @JoinTable(name = "black_list",
            joinColumns = @JoinColumn(name = "blocker_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id", referencedColumnName = "id"))
    private List<User> blacklistedUsers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "favorite_advertisement",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "advertisement_id", referencedColumnName = "id"))
    private List<Advertisement> favoriteAdvertisements;

    @OneToOne(mappedBy = "user")
    @Accessors(chain = true)
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private EmailConfirmationCode emailConfirmationCode;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof User user) {
            return Objects.equals(getUsername(), user.getUsername())
                    && Objects.equals(getEmail(), user.getEmail())
                    && Objects.equals(getFirstName(), user.getFirstName())
                    && Objects.equals(getLastName(), user.getLastName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
