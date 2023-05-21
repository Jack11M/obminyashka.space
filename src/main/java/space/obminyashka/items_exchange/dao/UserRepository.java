package space.obminyashka.items_exchange.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailOrUsername(String username, String email);

    @Query("select u.updated from User u where u.username = :username or u.email = :username")
    LocalDateTime selectLastUpdatedTimeFromUserByUsername(String username);

    @Query("select u.password from User u where u.username = :username or u.email = :username")
    String getUserPasswordByUsername(String username);

    @Query("select u.email from User u where u.username = :username or u.email = :username")
    String getUserEmailByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query("select u from User u where u.username = :username or u.email = :username")
    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken_Token(String token);

    List<User> findByRole_Name(String name);

    @Transactional
    @Modifying
    @Query("update User u set u.avatarImage = null where u.username = :username")
    void cleanAvatarForUserByName(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.password = :password where u.username = :username")
    void saveUserPasswordByUsername(String username, String password);

    @Transactional
    @Modifying
    @Query("update User u set u.email = :email, u.isValidatedEmail = false" +
            " where u.username = :username or u.email = :username")
    void updateUserEmailAndConfirmationCodeByUsername(String username, String email);

    @Transactional
    @Modifying
    @Query(value = "insert into email_confirmation_code(id, user_id, expiry_date) " +
            "values(:codeId, (select id from user where username = :username or email = :username), :expiryDate)", nativeQuery = true)
    void saveUserEmailConfirmationCodeByUsername(String username, UUID codeId, LocalDateTime expiryDate);

    @Transactional
    @Modifying
    @Query("update User u set u.oauth2Login = true, u.isValidatedEmail = true where u.email = :email")
    void setOAuth2LoginToUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.isValidatedEmail = true where u = " +
            "(select user from email_confirmation_code where id = :id)")
    void setValidatedEmailToUserByEmailId(UUID id);

    @Transactional
    @Modifying
    @Query("update User u set u.role = (select r from Role r where r.name = :roleName) " +
            "where u.username = :username or u.email = :username")
    void updateUserByUsernameWithRole(String username, String roleName);

    @Transactional
    @Modifying
    @Query("update User u set u.avatarImage = :newAvatarImage " +
            "where u.username = :usernameOrEmail or u.email =:usernameOrEmail ")
    void updateAvatarByUsername(String usernameOrEmail, byte[] newAvatarImage);

}
