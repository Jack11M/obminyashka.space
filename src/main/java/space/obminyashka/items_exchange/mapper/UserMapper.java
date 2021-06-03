package space.obminyashka.items_exchange.mapper;

import space.obminyashka.items_exchange.dto.PhoneDto;
import space.obminyashka.items_exchange.dto.UserRegistrationDto;
import space.obminyashka.items_exchange.dto.UserUpdateDto;
import space.obminyashka.items_exchange.model.Phone;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    private static ModelMapper mapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        mapper = modelMapper;
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
        user.setLastOnlineTime(LocalDateTime.now());
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public static User convertDto(UserUpdateDto userUpdateDto) {
        Converter<String, Long> stringLongConverter = context ->
                Long.parseLong(context.getSource().replaceAll("[^\\d]", ""));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        mapper.typeMap(UserUpdateDto.class, User.class).addMappings(mapper -> mapper.skip(User::setRole));
        mapper.typeMap(PhoneDto.class, Phone.class)
                .addMappings(mapper -> mapper.using(stringLongConverter)
                        .map(PhoneDto::getPhoneNumber, Phone::setPhoneNumber));
        return mapper.map(userUpdateDto, User.class);
    }
}
