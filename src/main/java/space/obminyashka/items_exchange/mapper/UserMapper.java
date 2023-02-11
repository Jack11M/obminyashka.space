package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.dto.UserDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;

import space.obminyashka.items_exchange.model.User;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {PhoneMapper.class, ChildMapper.class})
public interface UserMapper {
    default String localeToString(Locale locale) {
        return Optional.ofNullable(locale)
                .map(Locale::toString)
                .orElseGet(() -> Locale.of("ua").toString());
    }

    default Locale stringToLocale(String string) {
        return Locale.of(string);
    }

    //Unmapped target properties: "id, created, updated, status, password, online, lastOnlineTime, role, advertisements, deals, phones, children, chats, messages, blacklistedUsers, authorities".
    @Mapping(target = "refreshToken", ignore = true)
    User toModel(UserLoginResponseDto dto);
    UserDto toDto(User model);

    //Unmapped target properties: "accessToken, accessTokenExpirationDate, refreshTokenExpirationDate".
    @Mapping(source = "refreshToken.token", target = "refreshToken")
    UserLoginResponseDto toLoginResponseDto(User model);

    List<UserLoginResponseDto> toDtoList(List<User> modelList);
    List<User> toModelList(List<UserLoginResponseDto> dtoList);
}
