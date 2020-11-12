package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.mapper.UserMapper;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.Phone;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.mapper.UserMapper.convertDto;
import static com.hillel.items_exchange.mapper.UtilMapper.convertAllTo;
import static com.hillel.items_exchange.mapper.UtilMapper.convertToDto;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private static final Set<String> READONLY_FIELDS = Set.of("username", "lastOnlineTime", "children", "phones");

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
        Collection<Child> children = extractAll(updatedUser.getChildren(), child -> child.getId() == 0, ArrayList::new);
        Collection<Phone> phones = extractAll(updatedUser.getPhones(), phone -> phone.getId() == 0, HashSet::new);
        if (!isNewUser(user) && (!children.isEmpty() || !phones.isEmpty())) {
            throw new IllegalOperationException(
                    getExceptionMessageSource("exception.illegal.field.change") + "children or phones");
        }
        checkReadOnlyFieldsUpdate(updatedUser, user);
        BeanUtils.copyProperties(newUserDto, user,
                "id", "created", "updated", "status", "username", "password", "online",
                "lastOnlineTime", "role", "advertisements", "deals", "phones", "children");
        user.setUpdated(LocalDate.now());
        addNewChildren(user, children);
        addNewPhones(user, phones);
        return mapUserToDto(userRepository.saveAndFlush(user));
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

    public void addChildren(User parent, List<ChildDto> childrenDtoToAdd) {
        final List<Child> childrenToSave = new ArrayList<>(convertAllTo(
                childrenDtoToAdd, Child.class, ArrayList::new));
        addNewChildren(parent, childrenToSave);
        userRepository.save(parent);
    }

    public void updateChildren(User parent, List<ChildDto> childrenDtoToUpdate) {
        parent.getChildren().forEach(pChild -> childrenDtoToUpdate.forEach(uChild -> {
            if (pChild.getId() == uChild.getId()) {
                BeanUtils.copyProperties(uChild, pChild);
            }
        }));
        userRepository.saveAndFlush(parent);
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
                    getExceptionMessageSource("exception.illegal.field.change") + errorResponse);
        }
    }

    @SneakyThrows
    private boolean checkReadOnlyFields(User toCompare, User original, String fieldName) {
        Field declaredField = User.class.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return declaredField.get(toCompare).equals(declaredField.get(original));
    }

    private boolean isNewUser(User user) {
        return user.getUpdated().equals(user.getCreated());
    }

    private <T> Collection<T> extractAll(Collection<T> src, Predicate<T> predicate,
                                         Supplier<Collection<T>> collectionFactory) {
        Collection<T> extracted = src.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(collectionFactory));
        src.removeAll(extracted);
        return extracted;
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
