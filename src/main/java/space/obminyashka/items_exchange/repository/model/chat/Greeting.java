package space.obminyashka.items_exchange.repository.model.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Greeting {
    private String message;

    public Greeting(String message) {
        this.message = message;
    }

}
