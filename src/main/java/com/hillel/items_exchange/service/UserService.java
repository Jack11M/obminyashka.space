package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.*;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.Phone;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.BeanUtil;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.mapper.UserMapper.convertDto;
import static com.hillel.items_exchange.mapper.UtilMapper.convertAllTo;
import static com.hillel.items_exchange.mapper.UtilMapper.convertToDto;
import static com.hillel.items_exchange.model.enums.Status.ACTIVE;
import static com.hillel.items_exchange.model.enums.Status.DELETED;
import static com.hillel.items_exchange.util.Collections.extractAll;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionParametrizedMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String ONE_TIME_PER_DAY_AT_1AM = "0 0 3 * * * ";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private static final Set<String> READONLY_FIELDS = Set.of("username", "lastOnlineTime", "children", "phones");

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    public boolean existsByUsernameOrEmailAndPassword(String usernameOrEmail, String encryptedPassword) {
        Pattern emailPattern = Pattern.compile(PatternHandler.EMAIL);
        Optional<User> user = emailPattern.matcher(usernameOrEmail).matches()
                ? userRepository.findByEmail(usernameOrEmail)
                : userRepository.findByUsername(usernameOrEmail);
        return user.filter(u -> isPasswordMatches(u, encryptedPassword)).isPresent();
    }

    public boolean registerNewUser(UserRegistrationDto userRegistrationDto, Role role) {
        User registeredUser = UserMapper.userRegistrationDtoToUser(userRegistrationDto, bCryptPasswordEncoder, role);
        return userRepository.save(registeredUser).getId() != 0;
    }

    public UserDto update(UserDto newUserDto, User user) throws IllegalOperationException {
        User updatedUser = convertDto(newUserDto);
        var newChildren = extractAll(updatedUser.getChildren(), child -> child.getId() == 0, ArrayList::new);
        var newPhones = extractAll(updatedUser.getPhones(), phone -> phone.getId() == 0, HashSet::new);

        checkIsAllowedToAddNewChildrenOrPhones(user, !newChildren.isEmpty(), !newPhones.isEmpty());
        checkReadOnlyFieldsUpdate(updatedUser, user);

        BeanUtil.copyProperties(updatedUser, user, "email", "firstName", "lastName", "avatarImage");
        user.setUpdated(LocalDateTime.now());
        addNewChildren(user, newChildren);
        addNewPhones(user, newPhones);
        return mapUserToDto(userRepository.saveAndFlush(user));
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

    public String deleteUser(User user) {
        user.setStatus(DELETED);
        userRepository.saveAndFlush(user);

        return getExceptionParametrizedMessageSource("account.deleted", getDaysBeforeDeletion(user));
    }

    public long getDaysBeforeDeletion(User user) {
        return numberOfDaysToKeepDeletedUsers - (DAYS.between(user.getUpdated(), LocalDateTime.now()));
    }

    @Scheduled(cron = ONE_TIME_PER_DAY_AT_1AM)
    public void permanentlyDeleteUsers() {
        userRepository.findAll().stream()
                .filter(user -> user.getStatus().equals(DELETED) &&
                        isDurationMoreThanNumberOfDaysToKeepDeletedUser(user.getUpdated()))
                .forEach(userRepository::delete);
    }

    private boolean isDurationMoreThanNumberOfDaysToKeepDeletedUser(LocalDateTime localDateTime) {
        Duration duration = Duration.between(localDateTime, LocalDateTime.now());

        return duration.toDays() > numberOfDaysToKeepDeletedUsers;
    }

    public String restoreUser(User user) {
        user.setStatus(ACTIVE);
        userRepository.saveAndFlush(user);

        return getMessageSource("account.restored");
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

    private void checkReadOnlyFieldsUpdate(User toCompare, User original) throws IllegalOperationException {
        String errorResponse = READONLY_FIELDS.stream()
                .filter(fieldName -> !checkReadOnlyFields(toCompare, original, fieldName))
                .collect(Collectors.joining(", "));

        if (!errorResponse.isEmpty()) {
            throw new IllegalOperationException(
                    getMessageSource("exception.illegal.field.change") + errorResponse);
        }
    }

    @SneakyThrows
    private boolean checkReadOnlyFields(User toCompare, User original, String fieldName) {
        Field declaredField = User.class.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return declaredField.get(toCompare).equals(declaredField.get(original));
    }

    private void checkIsAllowedToAddNewChildrenOrPhones(User user, boolean hasNewChildren, boolean hasNewPhones)
            throws IllegalOperationException {
        boolean isNewUser = user.getUpdated().equals(user.getCreated());
        if ((!isNewUser) && (hasNewChildren || hasNewPhones)) {
            throw new IllegalOperationException(
                    getMessageSource("exception.illegal.children.phones.change"));
        }
    }

    private void addNewChildren(User user, Collection<Child> children) {
        children.forEach(child -> child.setUser(user));
        user.getChildren().addAll(children);
    }

    private void addNewPhones(User user, Collection<Phone> phones) {
        phones.forEach(phone -> phone.setUser(user));
        user.getPhones().addAll(phones);
    }
}
