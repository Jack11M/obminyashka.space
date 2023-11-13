package space.obminyashka.items_exchange.repository.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Message {
    @Id
    private UUID id;
    private String chatId;
    private String authorId;
    private String text;
    private LocalDateTime sendingTime;
    private String status;
    private List<Attachment> attachments;
}
