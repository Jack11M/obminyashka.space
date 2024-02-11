package space.obminyashka.items_exchange.repository.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import space.obminyashka.items_exchange.repository.model.base.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
public class Deal extends BaseEntity {

    @ManyToMany(mappedBy = "deals")
    private List<User> users;
}
