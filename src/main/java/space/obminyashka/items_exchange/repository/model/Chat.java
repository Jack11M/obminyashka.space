package space.obminyashka.items_exchange.repository.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Chat {
    @Id
    private UUID id;
    private String senderId;
    private String receiverId;
    private List<String> messageIdList;
    private LocalDateTime startDate;
}
