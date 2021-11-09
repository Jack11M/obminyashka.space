package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.authorization.jwt.JwtUser;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.RoleService;
import space.obminyashka.items_exchange.service.UserService;
import space.obminyashka.items_exchange.util.PatternHandler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.DAYS;
import static space.obminyashka.items_exchange.mapper.UtilMapper.convertAllTo;
import static space.obminyashka.items_exchange.mapper.UtilMapper.convertToDto;
import static space.obminyashka.items_exchange.model.enums.Status.ACTIVE;
import static space.obminyashka.items_exchange.model.enums.Status.DELETED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String ROLE_USER = "ROLE_USER";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("IN UserDetailsService (loadUserByUsername): " +
                        "user with username: " + username + " not found"));

        JwtUser jwtUser = create(user);
        log.info("IN UserDetailsService (loadUserByUsername): user with username: {} successfully loaded", username);
        return jwtUser;
    }

    private static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().getName())),
                user.getStatus().equals(Status.ACTIVE),
                user.getUpdated()
        );
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByEmailOrUsername(username, email);
    }

    @Override
    public Optional<User> findByUsernameOrEmailAndPassword(String usernameOrEmail, String encodedPassword) {
        Pattern usernamePattern = Pattern.compile(PatternHandler.USERNAME);
        Optional<User> user = usernamePattern.matcher(usernameOrEmail).matches()
                ? userRepository.findByUsername(usernameOrEmail)
                : userRepository.findByEmail(usernameOrEmail);

        return user.filter(u -> isPasswordMatches(u, encodedPassword));
    }

    @Override
    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encryptedPassword) {
        return findByUsernameOrEmailAndPassword(usernameOrEmail, encryptedPassword).isPresent();
    }

    @Override
    public boolean registerNewUser(UserRegistrationDto userRegistrationDto) {
        User userToRegister = userRegistrationDtoToUser(userRegistrationDto);
        final var locale = LocaleContextHolder.getLocale();
        userToRegister.setLanguage(locale);
        return userRepository.save(userToRegister).getId() != 0;
    }

    @Override
    public User loginUserWithOAuth2(DefaultOidcUser oauth2User) {
        var optionalUser = findByUsernameOrEmail(oauth2User.getEmail());
        return optionalUser.orElseGet(() -> userRepository.save(mapOAuth2UserToUser(oauth2User)));
    }

    private User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        var user = new User();
        BeanUtils.copyProperties(userRegistrationDto, user);
        return setUserFields(user, userRegistrationDto.getPassword(), "", "");
    }

    private User mapOAuth2UserToUser(DefaultOidcUser oAuth2User) {
        final var user = new User();
        final var email = oAuth2User.getEmail();
        final var firstName = Objects.requireNonNullElse(oAuth2User.getGivenName(), "");
        final var lastName = Objects.requireNonNullElse(oAuth2User.getFamilyName(), "");
        final var password = Objects.requireNonNullElse(oAuth2User.getIdToken().getTokenValue(),
                UUID.randomUUID().toString());
        user.setEmail(email);
        user.setUsername(email);
        return setUserFields(user, password, firstName, lastName);
    }

    private User setUserFields(User user, String password, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        roleService.getRole(ROLE_USER).ifPresent(user::setRole);
        user.setOnline(false);
        user.setAvatarImage(new byte[0]);
        var now = LocalDateTime.now();
        user.setLastOnlineTime(now);
        user.setStatus(Status.ACTIVE);
        return user;
    }

    @Override
    public String update(UserUpdateDto newUserUpdateDto, User user) {
        user.setFirstName(newUserUpdateDto.getFirstName());
        user.setLastName(newUserUpdateDto.getLastName());

        final var phonesToUpdate = convertPhone(newUserUpdateDto.getPhones());
        final var userPhones = user.getPhones();
        boolean isEqualsPhones = userPhones.equals(phonesToUpdate);
        if (!isEqualsPhones) {
            userPhones.removeIf(Predicate.not((phonesToUpdate::contains)));
            userPhones.addAll(phonesToUpdate);
            userPhones.forEach((phone -> phone.setUser(user)));
        }

        userRepository.saveAndFlush(user);
        return getMessageSource("changed.user.info");
    }

    @Override
    public String updateUserPassword(UserChangePasswordDto userChangePasswordDto, User user) {
        user.setPassword(bCryptPasswordEncoder.encode(userChangePasswordDto.getNewPassword()));
        userRepository.saveAndFlush(user);

        return getMessageSource("changed.user.password");
    }

    @Override
    public String updateUserEmail(UserChangeEmailDto userChangeEmailDto, User user) {
        user.setEmail(userChangeEmailDto.getNewEmail());
        userRepository.saveAndFlush(user);

        return getMessageSource("changed.user.email");
    }

    @Override
    public void selfDeleteRequest(User user) {
        user.setStatus(DELETED);
        userRepository.saveAndFlush(user);
    }

    @Override
    public long getDaysBeforeDeletion(User user) {
        return numberOfDaysToKeepDeletedUsers - (DAYS.between(user.getUpdated(), LocalDateTime.now()));
    }

    @Override
    @Scheduled(cron = "${cron.expression.once_per_day_at_3am}")
    public void permanentlyDeleteUsers() {
        userRepository.findAll().stream()
                .filter(Predicate.not(User::isEnabled))
                .filter(this::isDurationMoreThanNumberOfDaysToKeepDeletedUser)
                .forEach(userRepository::delete);
    }

    private boolean isDurationMoreThanNumberOfDaysToKeepDeletedUser(User user) {
        Duration duration = Duration.between(user.getUpdated(), LocalDateTime.now());

        return duration.toDays() > numberOfDaysToKeepDeletedUsers;
    }

    @Override
    public void makeAccountActiveAgain(User user) {
        user.setStatus(ACTIVE);
        userRepository.saveAndFlush(user);
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
    public boolean isPasswordMatches(User user, String encodedPassword) {
        return bCryptPasswordEncoder.matches(encodedPassword, user.getPassword());
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<ChildDto> getChildren(User parent) {
        return convertToDto(parent.getChildren(), ChildDto.class);
    }

    @Override
    public List<ChildDto> addChildren(User parent, List<ChildDto> childrenDtoToAdd) {
        final List<Child> childrenToSave = new ArrayList<>(convertAllTo(
                childrenDtoToAdd, Child.class, ArrayList::new));
        addNewChildren(parent, childrenToSave);
        userRepository.save(parent);
        List<Child> children = parent.getChildren();
        children.retainAll(childrenToSave);
        return convertToDto(children, ChildDto.class);
    }

    @Override
    public List<ChildDto> updateChildren(User parent, List<ChildDto> childrenDtoToUpdate) {
        List<Child> updatedChildren = new ArrayList<>();
        parent.getChildren().forEach(pChild -> childrenDtoToUpdate.forEach(uChild -> {
            if (pChild.getId() == uChild.getId()) {
                BeanUtils.copyProperties(uChild, pChild);
                updatedChildren.add(pChild);
            }
        }));
        userRepository.saveAndFlush(parent);
        return convertToDto(updatedChildren, ChildDto.class);
    }

    @Override
    public void removeChildren(User parent, List<Long> childrenIdToRemove) {
        parent.getChildren().removeIf(child -> childrenIdToRemove.contains(child.getId()));
        userRepository.saveAndFlush(parent);
    }

    @Override
    public void setUserAvatar(byte[] newAvatarImage, User user) {
        user.setAvatarImage(newAvatarImage);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updatePreferableLanguage(String refreshToken) {
        final var locale = LocaleContextHolder.getLocale();
        userRepository.findByRefreshToken_Token(refreshToken)
                .filter(user -> !user.getLanguage().equals(locale))
                .ifPresent(user -> {
                    user.setLanguage(locale);
                    userRepository.saveAndFlush(user);
                    log.info("Preferable language was successfully updated for User: {}", user.getUsername());
                });
    }

    private void addNewChildren(User user, Collection<Child> children) {
        children.forEach(child -> child.setUser(user));
        user.getChildren().addAll(children);
    }

    private Set<Phone> convertPhone(Set<PhoneDto> phones) {
        Converter<String, Long> stringLongConverter = context ->
                Long.parseLong(context.getSource().replaceAll("[^\\d]", ""));

        modelMapper.typeMap(PhoneDto.class, Phone.class)
                .addMappings(mapper -> mapper.using(stringLongConverter)
                        .map(PhoneDto::getPhoneNumber, Phone::setPhoneNumber));
        return modelMapper.map(phones, new TypeToken<Set<Phone>>() {
        }.getType());
    }
}
