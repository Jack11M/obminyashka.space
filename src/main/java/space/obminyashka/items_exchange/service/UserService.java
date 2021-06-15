package space.obminyashka.items_exchange.service;

import org.springframework.scheduling.annotation.Scheduled;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;

import java.util.List;
import java.util.Optional;

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
     * Register new user with received role
     * @param userRegistrationDto DTO which contains all required data for registration the user
     * @param role provided role which has to be set to the user
     * @return result of registration
     */
    boolean registerNewUser(UserRegistrationDto userRegistrationDto, Role role);

    /**
     * Update an existed user with new data
     * @param newUserUpdateDto new data for update
     * @param user existed user to update
     * @return a message as the result of the operation
     */
    String update(UserUpdateDto newUserUpdateDto, User user);

    /**
     * Update password of an existed user with new one
     * @param userChangePasswordDto DTO which contains old password and doubled new password
     * @param user existed user to update
     * @return a message as the result of the operation
     */
    String updateUserPassword(UserChangePasswordDto userChangePasswordDto, User user);

    /**
     * Update email of an existed user with new one
     * @param userChangeEmailDto DTO which contains doubled new email
     * @param user existed user to update
     * @return a message as the result of the operation
     */
    String updateUserEmail(UserChangeEmailDto userChangeEmailDto, User user);

    /**
     * Request from a user to remove them account with time limit
     * @param user existed user which is requested self-removing procedure
     */
    void selfDeleteRequest(User user);

    /**
     * Getting days which is/are left for the user before removing from DB
     * @param user the requested user
     * @return quantity of days that is/are left
     */
    long getDaysBeforeDeletion(User user);

    /**
     * Scheduled job which checks users that needs to be removed from DB after exhaustion of the grace period
     */
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    void permanentlyDeleteUsers();

    /**
     * Restoration of the user which was tagged as "to be removed"
     * @param user the requested user which has to be active again
     */
    void makeAccountActiveAgain(User user);

    /**
     * Check whether the user exist into DB by login
     * @param username login of the user to check
     * @return result of the check
     */
    boolean existsByUsername(String username);

    /**
     * Check whether the user exist into DB by email
     * @param email mail address of the user to check
     * @return result of the check
     */
    boolean existsByEmail(String email);

    /**
     * Check whether the user exist into DB by email or username and encrypted user's password
     * @param usernameOrEmail login or email to be checked
     * @param encodedPassword encoded password of the user
     * @return result of the check
     */
    boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encodedPassword);

    /**
     * Check whether received user's password matches to gained user
     * @param user user to check password
     * @param encodedPassword encoded password
     * @return result of the check
     */
    boolean isPasswordMatches(User user, String encodedPassword);

    /**
     * Get all children from gained user
     * @param parent the user which is requested for getting all the children
     * @return all existed children for provided user
     */
    List<ChildDto> getChildren(User parent);

    /**
     * Add new children to existed user
     * @param parent the user which is required for adding new children
     * @param childrenDtoToAdd list which contains new children's data
     * @return children which were added to the user
     */
    List<ChildDto> addChildren(User parent, List<ChildDto> childrenDtoToAdd);

    /**
     * Update existed children of gained user
     * @param parent user for children update
     * @param childrenDtoToUpdate updated children data
     * @return children which were updated
     */
    List<ChildDto> updateChildren(User parent, List<ChildDto> childrenDtoToUpdate);

    /**
     * Remove received children from gained user
     * @param parent user for removing children
     * @param childrenIdToRemove list which contains children which have to be removed
     */
    void removeChildren(User parent, List<Long> childrenIdToRemove);

    /**
     * Set received image to existed user as the avatar image
     * @param newAvatarImage image represented into array of bytes
     * @param user user whom the image has to be set as new avatar image
     */
    void setUserAvatar(byte[] newAvatarImage, User user);
}
