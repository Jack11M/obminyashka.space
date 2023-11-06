package space.obminyashka.items_exchange.repository.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Accessors(chain = true)
public class Attachment {
    @Id
    private String id;
    private List<byte[]> images;
}
