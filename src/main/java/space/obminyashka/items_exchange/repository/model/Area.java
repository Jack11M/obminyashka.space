package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Area {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name_ua", nullable = false)
    private String nameUa;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

}
