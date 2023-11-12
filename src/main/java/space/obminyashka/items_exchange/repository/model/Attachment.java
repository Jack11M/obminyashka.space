package space.obminyashka.items_exchange.repository.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Attachment {
    @Id
    private UUID id;
    private List<byte[]> images;
}
