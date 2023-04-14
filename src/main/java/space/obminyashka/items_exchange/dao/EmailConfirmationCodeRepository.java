package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;

import java.util.UUID;

@Repository
public interface EmailConfirmationCodeRepository extends JpaRepository<EmailConfirmationCode, UUID> {

}
