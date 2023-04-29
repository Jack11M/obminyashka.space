package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.dto.UserDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.mapper.ChildMapper;
import space.obminyashka.items_exchange.mapper.PhoneMapper;
import space.obminyashka.items_exchange.mapper.UserMapper;
import space.obminyashka.items_exchange.model.EmailConfirmationCode;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.DAYS;
import static space.obminyashka.items_exchange.model.enums.Status.UPDATED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String ROLE_USER = "ROLE_USER";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ChildMapper childMapper;
    private final PhoneMapper phoneMapper;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    @Value("${number.of.hours.to.keep.email.confirmation.code}")
    private final int numberOfHoursToKeepEmailConformationCode;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("IN UserDetailsService (loadUserByUsername): " +
                        "user with username: " + username + " not found"));

        log.info("IN UserDetailsService (loadUserByUsername): user with username: {} successfully loaded", username);
        return user;
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    @Override
    public boolean registerNewUser(UserRegistrationDto userRegistrationDto, UUID codeId) {
        User userToRegister = userRegistrationDtoToUser(userRegistrationDto);
        userToRegister.setEmailConfirmationCode(new EmailConfirmationCode(codeId, numberOfHoursToKeepEmailConformationCode));
        return userRepository.save(userToRegister).getId() != null;
    }

    private User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        return User.builder()
                .username(userRegistrationDto.getUsername())
                .email(userRegistrationDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()))
                .role(roleService.getRole(ROLE_USER).orElse(null))
                .build();
    }

    @Override
    public User loginUserWithOAuth2(DefaultOidcUser oauth2User) {
        var optionalUser = findByUsernameOrEmail(oauth2User.getEmail());
        optionalUser.filter(Predicate.not(this::isUserHasNoOAuthOrValidatedEmail))
                .map(User::getEmail)
                .ifPresent(userRepository::setOAuth2LoginToUserByEmail);

        return optionalUser.orElseGet(() -> userRepository.save(mapOAuth2UserToUser(oauth2User)));
    }

    private boolean isUserHasNoOAuthOrValidatedEmail(User user) {
        return Objects.requireNonNullElse(user.getOauth2Login(), false)
                || Objects.requireNonNullElse(user.isValidatedEmail(), false);
    }

    private User mapOAuth2UserToUser(DefaultOidcUser oAuth2User) {
        final var email = oAuth2User.getEmail();
        final var pass = Objects.requireNonNullElse(oAuth2User.getIdToken().getTokenValue(), UUID.randomUUID().toString());
        return User.builder()
                .oauth2Login(true)
                .username(email)
                .email(email)
                .isValidatedEmail(oAuth2User.getEmailVerified())
                .password(bCryptPasswordEncoder.encode(pass))
                .firstName(oAuth2User.getGivenName())
                .lastName(oAuth2User.getFamilyName())
                .lastOnlineTime(LocalDateTime.now())
                .online(true)
                .role(roleService.getRole(ROLE_USER).orElse(null))
                .build();
    }

    @Override
    public String update(UserUpdateDto newUserUpdateDto, User user) {
        BeanUtils.copyProperties(newUserUpdateDto, user, "phones");
        final var phonesToUpdate = phoneMapper.toModelSet(newUserUpdateDto.getPhones());
        final var userPhones = user.getPhones();
        userPhones.clear();
        userPhones.addAll(phonesToUpdate);
        phonesToUpdate.forEach(phone -> phone.setUser(user));

        user.setStatus(UPDATED);
        userRepository.saveAndFlush(user);
        return getMessageSource(ResponseMessagesHandler.PositiveMessage.CHANGED_USER_INFO);
    }

    @Override
    public void update(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUserPassword(String username, String password) {
        userRepository.saveUserPasswordByUsername(username, bCryptPasswordEncoder.encode(password));
    }

    @Override
    public void updateUserEmail(String username, String email) {
        userRepository.saveUserEmailByUsername(username, email);
    }

    @Override
    public void selfDeleteRequest(String username) {
        userRepository.updateUserByUsernameWithRole(username, "ROLE_SELF_REMOVING");
        log.info("[UserServiceImpl] User '{}' is now in SELF REMOVING role", username);
    }

    @Override
    public long getDaysBeforeDeletion(String username) {
        final var userLastUpdateTime = userRepository.selectLastUpdatedTimeFromUserByUsername(username);
        return calculateDaysBeforeCompleteRemove(userLastUpdateTime);
    }

    @Override
    public long calculateDaysBeforeCompleteRemove(LocalDateTime userLastUpdateTime) {
        return numberOfDaysToKeepDeletedUsers - (DAYS.between(userLastUpdateTime, LocalDateTime.now()));
    }

    @Override
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    public void permanentlyDeleteUsers() {
        userRepository.findByRole_Name("ROLE_SELF_REMOVING").stream()
                .filter(this::isDurationMoreThanNumberOfDaysToKeepDeletedUser)
                .forEach(userRepository::delete);
    }

    private boolean isDurationMoreThanNumberOfDaysToKeepDeletedUser(User user) {
        Duration duration = Duration.between(user.getUpdated(), LocalDateTime.now());

        return duration.toDays() > numberOfDaysToKeepDeletedUsers;
    }

    @Override
    public void makeAccountActiveAgain(String username) {
        roleService.setUserRoleToUserByUsername(username);
        log.info("[UserServiceImpl] User '{}' is active once again", username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(this::mapUserToDto);
    }

    @Override
    public boolean isUserPasswordMatches(String username, String password) {
        return bCryptPasswordEncoder.matches(password, userRepository.getUserPasswordByUsername(username));
    }

    @Override
    public boolean isUserEmailMatches(String username, String email) {
        return userRepository.getUserEmailByUsername(username).equals(email);
    }

    private UserDto mapUserToDto(User user) {
        return userMapper.toDto(user);
    }

    @Override
    public List<ChildDto> getChildren(User parent) {
        return childMapper.toDtoList(parent.getChildren());
    }

    @Override
    public List<ChildDto> updateChildren(User parent, List<ChildDto> childrenDtoToUpdate) {
        final var childrenToSave = childMapper.toModelList(childrenDtoToUpdate);
        final var existedChildren = parent.getChildren();
        existedChildren.clear();
        existedChildren.addAll(childrenToSave);
        childrenToSave.forEach(child -> child.setUser(parent));

        userRepository.saveAndFlush(parent);
        return childrenDtoToUpdate;
    }

    @Override
    public void setUserAvatar(byte[] newAvatarImage, User user) {
        user.setAvatarImage(newAvatarImage);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void removeUserAvatarFor(String username) {
        userRepository.cleanAvatarForUserByName(username);
    }

    @Override
    public void updatePreferableLanguage(String refreshToken) {
        final var locale = LocaleContextHolder.getLocale();
        userRepository.findByRefreshToken_Token(refreshToken)
                .filter(Predicate.not(user -> Objects.equals(user.getLanguage(), locale)))
                .ifPresent(user -> {
                    user.setLanguage(locale);
                    userRepository.saveAndFlush(user);
                    log.info("Preferable language was successfully updated for User: {}", user.getUsername());
                });
    }
}
