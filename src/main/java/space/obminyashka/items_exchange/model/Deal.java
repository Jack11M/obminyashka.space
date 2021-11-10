package space.obminyashka.items_exchange.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Deal extends BaseEntity {

    @ManyToMany(mappedBy = "deals")
    private List<User> users;
}
