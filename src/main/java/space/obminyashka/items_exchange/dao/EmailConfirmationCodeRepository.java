package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;


import java.util.UUID;

public interface EmailConfirmationCodeRepository extends JpaRepository<EmailConfirmationCode, UUID> {

}
