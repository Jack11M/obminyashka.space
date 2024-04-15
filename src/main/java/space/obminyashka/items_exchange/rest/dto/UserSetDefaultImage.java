package space.obminyashka.items_exchange.rest.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.obminyashka.items_exchange.repository.model.Advertisement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserSetDefaultImage {
    private UUID id;
    private String username;
    private String email;
    private List<Advertisement> advertisements;
    private byte[] avatarImage = new byte[0];
}
