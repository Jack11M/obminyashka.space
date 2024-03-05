package space.obminyashka.items_exchange.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.EmailConfirmationCode;


import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface EmailConfirmationCodeRepository extends JpaRepository<EmailConfirmationCode, UUID> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into email_confirmation_code(id, user_id, expiry_date, type)" +
            "values(:codeId, (select u.id from User u where u.email = :email), :expiryData, :type)")
    void saveConfirmationCode(UUID codeId, String email, LocalDateTime expiryData, String type);
}
