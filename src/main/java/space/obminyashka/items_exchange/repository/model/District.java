package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class District {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @Column(name = "name_ua", nullable = false)
    private String nameUa;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

}
