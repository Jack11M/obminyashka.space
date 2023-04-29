package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import space.obminyashka.items_exchange.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

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
