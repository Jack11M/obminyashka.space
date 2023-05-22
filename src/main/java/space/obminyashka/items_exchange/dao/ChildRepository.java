package space.obminyashka.items_exchange.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.model.Child;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChildRepository extends JpaRepository<Child, UUID> {
    List<Child> findByUser_Username(String username);

    @Transactional
    @Modifying
    void deleteByUser_Username(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into Child values (:id, " +
            "(select id from user where username = :username), :birthDay, :sex)")
    void createChildrenByUsername(UUID id, LocalDate birthDay, String sex, String username);

    default void createChildrenByUsername(Child child, String username) {
        this.createChildrenByUsername(UUID.randomUUID(), child.getBirthDate(), child.getSex().name(), username);
    }
}
