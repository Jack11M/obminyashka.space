package space.obminyashka.items_exchange.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

    @Transactional
    @Modifying
    @Query("update User set role.id =(select id from Role where name='ROLE_USER') " +
            "where username = :username or email = :username")
    void setUserRoleToUserByUsername(String username);

}
