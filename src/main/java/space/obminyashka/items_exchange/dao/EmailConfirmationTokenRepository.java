package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import space.obminyashka.items_exchange.model.EmailConfirmationToken;


import java.util.UUID;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, UUID> {

}
