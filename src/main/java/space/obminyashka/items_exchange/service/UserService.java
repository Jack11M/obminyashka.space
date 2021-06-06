package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.util.PatternHandler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static space.obminyashka.items_exchange.mapper.UtilMapper.*;
import static space.obminyashka.items_exchange.model.enums.Status.ACTIVE;
import static space.obminyashka.items_exchange.model.enums.Status.DELETED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encryptedPassword) {
        Pattern usernamePattern = Pattern.compile(PatternHandler.USERNAME);
        Optional<User> user = usernamePattern.matcher(usernameOrEmail).matches()
                ? userRepository.findByUsername(usernameOrEmail)
                : userRepository.findByEmail(usernameOrEmail);

        return user.filter(u -> isPasswordMatches(u, encryptedPassword)).isPresent();
    }

    public boolean registerNewUser(UserRegistrationDto userRegistrationDto, Role role) {
        User registeredUser = userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        return userRepository.save(registeredUser).getId() != 0;
    }

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
        convertTo(userRepository.saveAndFlush(user), UserUpdateDto.class);
        return getMessageSource("changed.user.info");
    }

    public String updateUserPassword(UserChangePasswordDto userChangePasswordDto, User user) {
        user.setPassword(bCryptPasswordEncoder.encode(userChangePasswordDto.getNewPassword()));
        userRepository.saveAndFlush(user);

        return getMessageSource("changed.user.password");
    }

    public String updateUserEmail(UserChangeEmailDto userChangeEmailDto, User user) {
        user.setEmail(userChangeEmailDto.getNewEmail());
        userRepository.saveAndFlush(user);

        return getMessageSource("changed.user.email");
    }

    public void selfDeleteRequest(User user) {
        user.setStatus(DELETED);
        userRepository.saveAndFlush(user);
    }

    public long getDaysBeforeDeletion(User user) {
        return numberOfDaysToKeepDeletedUsers - (DAYS.between(user.getUpdated(), LocalDateTime.now()));
    }

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

    public void makeAccountActiveAgain(User user) {
        user.setStatus(ACTIVE);
        userRepository.saveAndFlush(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<UserDto> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail).map(this::mapUserToDto);
    }

    public boolean isPasswordMatches(User user, String encodedPassword){
        return bCryptPasswordEncoder.matches(encodedPassword, user.getPassword());
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public List<ChildDto> getChildren(User parent) {
        return convertToDto(parent.getChildren(), ChildDto.class);
    }

    public List<ChildDto> addChildren(User parent, List<ChildDto> childrenDtoToAdd) {
        final List<Child> childrenToSave = new ArrayList<>(convertAllTo(
                childrenDtoToAdd, Child.class, ArrayList::new));
        addNewChildren(parent, childrenToSave);
        userRepository.save(parent);
        List<Child> children = parent.getChildren();
        children.retainAll(childrenToSave);
        return convertToDto(children, ChildDto.class);
    }

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

    public void removeChildren(User parent, List<Long> childrenIdToRemove) {
        parent.getChildren().removeIf(child -> childrenIdToRemove.contains(child.getId()));
        userRepository.saveAndFlush(parent);
    }

    public void setUserAvatar(byte[] newAvatarImage, User user) {
        user.setAvatarImage(newAvatarImage);
        userRepository.saveAndFlush(user);
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
        return modelMapper.map(phones, new TypeToken<Set<Phone>>() {}.getType());
    }

    public static User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto,
                                                 BCryptPasswordEncoder bCryptPasswordEncoder,
                                                 Role role) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(role);
        user.setFirstName("");
        user.setLastName("");
        user.setOnline(false);
        user.setAvatarImage(new byte[0]);
        user.setAdvertisements(Collections.emptyList());
        user.setChildren(Collections.emptyList());
        user.setDeals(Collections.emptyList());
        user.setPhones(Collections.emptySet());
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setUpdated(now);
        user.setLastOnlineTime(now);
        user.setStatus(Status.ACTIVE);
        return user;
    }
}
