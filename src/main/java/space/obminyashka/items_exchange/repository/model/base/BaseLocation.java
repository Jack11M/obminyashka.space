package space.obminyashka.items_exchange.repository.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
public abstract class BaseLocation {

    @Id
    @GeneratedValue
    protected UUID id;

    @Column(name = "name_ua", nullable = false)
    protected String nameUa;

    @Column(name = "name_en", nullable = false)
    protected String nameEn;

    protected BaseLocation() {
        this.id = UUID.randomUUID();
    }

    protected BaseLocation(String nameUa, String nameEn) {
        this();
        this.nameUa = decoratePossibleApostropheChars(nameUa);
        this.nameEn = decoratePossibleApostropheChars(nameEn);
    }

    private String decoratePossibleApostropheChars(String stringToDecorate) {
        return stringToDecorate.replace("’", "'").replace("'", "\\'");
    }

    public abstract String formatForSQL();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof BaseLocation that) {
            return Objects.equals(nameUa, that.nameUa) && Objects.equals(nameEn, that.nameEn);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameUa, nameEn);
    }
}
