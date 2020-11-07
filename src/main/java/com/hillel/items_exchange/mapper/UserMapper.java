package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.PhoneDto;
import com.hillel.items_exchange.dto.UserDto;
import com.hillel.items_exchange.dto.UserRegistrationDto;
import com.hillel.items_exchange.model.Phone;
import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.Status;
import com.hillel.items_exchange.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        user.setChildren(Collections.emptySet());
        user.setDeals(Collections.emptyList());
        user.setPhones(Collections.emptySet());
        LocalDate now = LocalDate.now();
        user.setCreated(now);
        user.setUpdated(now);
        user.setLastOnlineTime(LocalDateTime.now());
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public static User convertDto(UserDto userDto) {
        Converter<String, Long> stringLongConverter = context ->
                Long.parseLong(context.getSource().replaceAll("[^\\d]", ""));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        mapper.typeMap(UserDto.class, User.class).addMappings(mapper -> mapper.skip(User::setRole));
        mapper.typeMap(PhoneDto.class, Phone.class)
                .addMappings(mapper -> mapper.using(stringLongConverter)
                        .map(PhoneDto::getPhoneNumber, Phone::setPhoneNumber));
        return mapper.map(userDto, User.class);
    }
}
