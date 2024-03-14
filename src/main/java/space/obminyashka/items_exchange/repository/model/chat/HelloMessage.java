package space.obminyashka.items_exchange.repository.model.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HelloMessage {
    private String name;

    public HelloMessage(String name) {
        this.name = name;
    }

    public HelloMessage() {
        this.name = null;
    }

}
