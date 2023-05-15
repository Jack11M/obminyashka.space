package space.obminyashka.items_exchange.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.dto.UserDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * Find a user into DB by checking gained param either username or email columns
     * @param usernameOrEmail login or email of the user
     * @return {@link Optional} with the user as the result
     */
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    /**
     * Find a user into DB by checking gained username and convert it into DTO
     * @param username login of the user
     * @return {@link Optional} with the converted user as the result
     */
    Optional<UserDto> findByUsername(String username);

    /**
     * Register new user and create email confirmation code
     * @param userRegistrationDto DTO which contains all required data for registration the user
     * @param codeId UIID for confirmation email
     * @return result of registration
     */
    boolean registerNewUser(UserRegistrationDto userRegistrationDto, UUID codeId);

    /**
     * Update an existed user with new data
     * @param newUserUpdateDto new data for update
     * @param user existed user to update
     * @return a message as the result of the operation
     */
    String update(UserUpdateDto newUserUpdateDto, User user);

    /**
     * Update the user in the database.
     *
     * @param user The user object to be updated.
     */
    void update(User user);

    /**
     * Update the password for the user with the given username.
     * @param username The username of the user whose password you want to update.
     * @param password The new password for the user.
     */
    void updateUserPassword(String  username, String password);

    /**
     * Update the email for the user with the given username.
     * @param username The username of the user whose email you want to update.
     * @param email The new email for the user.
     * @param codeId The code for confirmation email.
     */
    void updateUserEmail(String username, String email, UUID codeId);

    /**
     * Request from a user to remove them account with time limit
     *
     * @param username login or email of existing user which is requested self-removing procedure
     */
    void selfDeleteRequest(String username);

    /**
     * Getting days which is/are left for the user before removing from DB
     * @param username the requested user login
     * @return quantity of days that is/are left
     */
    long getDaysBeforeDeletion(String username);

    /**
     * Getting days which is/are left for the user before removing from DB
     * @param lastUpdateTime the user last update time
     * @return quantity of days that is/are left
     */
    long calculateDaysBeforeCompleteRemove(LocalDateTime lastUpdateTime);

    /**
     * Scheduled job which checks users that needs to be removed from DB after exhaustion of the grace period
     */
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    void permanentlyDeleteUsers();

    /**
     * Restoration of the user which was tagged as "to be removed"
     * @param username the requested user which has to be active again
     */
    void makeAccountActiveAgain(String username);

    /**
     * Check whether the user exist into DB by email
     * @param email mail address of the user to check
     * @return result of the check
     */
    boolean existsByEmail(String email);

    /**
     * Check whether the user exist into DB by username or email
     * @param username username of the user to check
     * @param email mail address of the user to check
     * @return result of the check
     */
    boolean existsByUsernameOrEmail(String username, String email);

    /**
     * Given a username and password, return true if the user password and new password match, false otherwise.
     * @param username The username of the user.
     * @param password The password to be checked.
     * @return A boolean value.
     */
    boolean isUserPasswordMatches(String username, String password);

    /**
     * Given a username and email, return true if the user email and new email equals, false otherwise.
     * @param username The username of the user.
     * @param email The email to be checked.
     * @return A boolean value.
     */
    boolean isUserEmailMatches(String username, String email);

    /**
     * Get all children from gained user
     * @param parent the user which is requested for getting all the children
     * @return all existed children for provided user
     */
    List<ChildDto> getChildren(User parent);

    /**
     * Update existed children of gained user
     * @param username for children update
     * @param childrenDtoToUpdate updated children data
     * @return children which were updated
     */
    List<ChildDto> updateChildren(String username, List<ChildDto> childrenDtoToUpdate);

    /**
     * Set received image to existed user as the avatar image
     * @param newAvatarImage image represented into array of bytes
     * @param user user whom the image has to be set as new avatar image
     */
    void setUserAvatar(byte[] newAvatarImage, User user);

    /**
     * Remove avatar of selected user
     * @param username login of existing user
     */
    void removeUserAvatarFor(String username);

    /**
     * Get {@link org.springframework.http.HttpHeaders#ACCEPT_LANGUAGE} and compare with stored language setting
     * and update if they aren't equals
     * @param refreshToken token linked with stored user in the DB
     */
    void updatePreferableLanguage(String refreshToken);

    /**
     * Find {@link User} from OAuth2User credentials, register if user is new
     * @param oauth2User instance of {@link DefaultOidcUser} with the required credentials for user login/registration
     * @return {@link User} from OAuth2User credentials if exists, otherwise register a new user and return it
     */
    User loginUserWithOAuth2(DefaultOidcUser oauth2User);
}
