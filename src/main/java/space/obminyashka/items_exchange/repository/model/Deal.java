package space.obminyashka.items_exchange.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.repository.model.base.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
public class Deal extends BaseEntity {

    @ManyToMany(mappedBy = "deals")
    private List<User> users;
}
