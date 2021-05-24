package space.obminyashka.items_exchange.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.mapper.UserMapper;
import space.obminyashka.items_exchange.model.Child;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.util.PatternHandler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static space.obminyashka.items_exchange.mapper.UtilMapper.*;
import static space.obminyashka.items_exchange.model.enums.Status.ACTIVE;
import static space.obminyashka.items_exchange.model.enums.Status.DELETED;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

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
        User registeredUser = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        return userRepository.save(registeredUser).getId() != 0;
    }

    public UserWithoutChildrenDto updateUserWithoutChildren(UserWithoutChildrenDto userWithoutChildrenDto, User user)
            throws IllegalOperationException {

        addNewPhones(userWithoutChildrenDto, user);
        user.setFirstName(userWithoutChildrenDto.getFirstName());
        user.setLastName(userWithoutChildrenDto.getLastName());
        user.setUpdated(LocalDateTime.now());
        User updatedUser = userRepository.saveAndFlush(user);
        return convertTo(updatedUser, UserWithoutChildrenDto.class);
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

    public boolean isPasswordMatches(User user, String encodedPassword) {
        return bCryptPasswordEncoder.matches(encodedPassword, user.getPassword());
    }

    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public List<ChildDto> getChildren(User parent) {
        return convertToDto(parent.getChildren(), ChildDto.class);
    }

    public List<ChildDto> addChildren(User parent, List<ChildDto> childrenDtoToAdd) {
        final List<Child> childrenToSave = new ArrayList<>(convertAllTo(childrenDtoToAdd, Child.class, ArrayList::new));
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

    private void addNewPhones(UserWithoutChildrenDto userWithoutChildrenDto, User user) {
        Set<Long> newPhoneNumbers = userWithoutChildrenDto.getPhones().stream()
                .map(phoneDto -> Long.parseLong(phoneDto.getPhoneNumber().replaceAll("[^\\d]", "")))
                .collect(Collectors.toSet());
        Set<Long> existUserPhoneNumbers = user.getPhones().stream()
                .map(Phone::getPhoneNumber)
                .collect(Collectors.toSet());
        boolean isEqualsPhones = existUserPhoneNumbers.equals(newPhoneNumbers);
        if (!isEqualsPhones) {
            final var newPhones =
                    new HashSet<>(convertAllTo(userWithoutChildrenDto.getPhones(), Phone.class, HashSet::new));
            newPhones.forEach(phone -> phone.setUser(user));
            user.getPhones().clear();
            user.getPhones().addAll(newPhones);
        }
    }
}
