package space.obminyashka.items_exchange.service.impl;

import java.util.List;
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
import space.obminyashka.items_exchange.repository.EmailConfirmationCodeRepository;
import space.obminyashka.items_exchange.repository.UserRepository;
import space.obminyashka.items_exchange.repository.model.Advertisement;
import space.obminyashka.items_exchange.repository.model.EmailConfirmationCode;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.rest.mapper.PhoneMapper;
import space.obminyashka.items_exchange.rest.mapper.UserMapper;
import space.obminyashka.items_exchange.rest.request.MyUserInfoUpdateRequest;
import space.obminyashka.items_exchange.rest.request.UserRegistrationRequest;
import space.obminyashka.items_exchange.rest.response.MyUserInfoView;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.service.util.EmailType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.DAYS;
import static space.obminyashka.items_exchange.repository.enums.Status.UPDATED;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final String EMAIL_SUFFIX = "(?<=.{3}).(?=.*@)";
    private static final String ROLE_USER = "ROLE_USER";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final EmailConfirmationCodeRepository emailConfirmationCodeRepository;
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

        log.info("[UserServiceImpl] Invoked from UserDetailsService: User '{}' successfully loaded", user.getId());
        return user;
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    @Override
    public List<Advertisement> findListAdvertisementByUsername(String username) {
        return userRepository.findListAdvertisementByUsername(username);
    }

    @Override
    public UserLoginResponse findAuthDataByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findAuthDataByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .map(userMapper::toLoginResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException("User " + usernameOrEmail + " is not logged in"));
    }

    @Override
    public boolean registerNewUser(UserRegistrationRequest userRegistrationRequest, UUID codeId) {
        User userToRegister = userRegistrationDtoToUser(userRegistrationRequest);
        EmailConfirmationCode confirmationCode = new EmailConfirmationCode(codeId, userToRegister,
                numberOfHoursToKeepEmailConformationCode, EmailType.REGISTRATION.name());
        return emailConfirmationCodeRepository.save(confirmationCode).getUser().getId() != null;
    }

    private User userRegistrationDtoToUser(UserRegistrationRequest userRegistrationRequest) {
        return User.builder()
                .username(userRegistrationRequest.getUsername())
                .email(userRegistrationRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userRegistrationRequest.getPassword()))
                .role(roleService.getRole(ROLE_USER).orElse(null))
                .build();
    }

    @Override
    public User loginUserWithOAuth2(DefaultOidcUser oauth2User) {
        return userRepository.findUserProjectionByEmail(oauth2User.getEmail())
                .map(userMapper::toUserFromProjection)
                .orElseGet(() -> userRepository.save(mapOAuth2UserToUser(oauth2User)));
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
    public String update(MyUserInfoUpdateRequest newMyUserInfoUpdateRequest, User user) {
        BeanUtils.copyProperties(newMyUserInfoUpdateRequest, user, "phones");
        final var phonesToUpdate = phoneMapper.toModelSet(newMyUserInfoUpdateRequest.getPhones());
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
    public void saveCodeForResetPassword(String email, UUID codeId) {
        emailConfirmationCodeRepository.saveConfirmationCode(codeId, email,
                LocalDateTime.now().plusHours(numberOfHoursToKeepEmailConformationCode), EmailType.RESET.name());
    }

    @Override
    public void updateUserEmail(String username, String email, UUID codeId) {
        userRepository.updateUserEmailConfirmationCodeByUsername(username, codeId,
                LocalDateTime.now().plusHours(numberOfHoursToKeepEmailConformationCode));
        userRepository.updateUserEmailAndConfirmationCodeByUsername(username, email);
    }

    @Override
    public void selfDeleteRequest(String username) {
        userRepository.updateUserByUsernameWithRole(username, "ROLE_SELF_REMOVING");
        log.info("[UserServiceImpl] User '{}' is now in SELF REMOVING role", username.replaceAll(EMAIL_SUFFIX, "*"));
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
        final var selfRemovingNearestDate = LocalDateTime.now().minusDays(numberOfDaysToKeepDeletedUsers);
        userRepository.deleteAll(
                userRepository.findAllByUpdatedLessThanEqualAndRoleName(selfRemovingNearestDate, "ROLE_SELF_REMOVING"));
    }

    @Override
    public void makeAccountActiveAgain(String username) {
        roleService.setUserRoleToUserByUsername(username);
        log.info("[UserServiceImpl] User '{}' is active once again", username.replaceAll(EMAIL_SUFFIX, "*"));
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
    public Optional<MyUserInfoView> findByUsername(String username) {
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

    private MyUserInfoView mapUserToDto(User user) {
        return userMapper.toDto(user);
    }

    @Override
    public void setUserAvatar(String username, byte[] newAvatarImage) {
        userRepository.updateAvatarByUsername(username, newAvatarImage);
    }

    @Override
    public void setValidatedEmailByUsernameOrEmail(String usernameOrEmail) {
        userRepository.setValidatedEmailByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
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
                    log.info("[UserServiceImpl] Preferable language was successfully updated for User: {}", user.getId());
                });
    }
}
