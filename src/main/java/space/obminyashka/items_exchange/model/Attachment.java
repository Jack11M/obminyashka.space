package space.obminyashka.items_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;
    private String type;
    @Column(name = "file_content")
    private byte[] fileContent;
}
