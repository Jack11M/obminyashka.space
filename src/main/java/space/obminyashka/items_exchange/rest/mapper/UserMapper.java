package space.obminyashka.items_exchange.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.repository.projection.UserAuthProjection;
import space.obminyashka.items_exchange.repository.projection.UserProjection;
import space.obminyashka.items_exchange.rest.response.MyUserInfoView;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PhoneMapper.class, ChildMapper.class})
public interface UserMapper {
    default String localeToString(Locale locale) {
        return Optional.ofNullable(locale)
                .map(Locale::toString)
                .orElseGet(() -> Locale.of("ua").toString());
    }

    default Locale stringToLocale(String string) {
        return Locale.of(string);
    }

    //Unmapped target properties: "id, created, updated, status, password, online, lastOnlineTime, role, advertisements, deals, phones, children, blacklistedUsers, authorities".
    @Mapping(target = "refreshToken", ignore = true)
    User toModel(UserLoginResponse dto);

    MyUserInfoView toDto(User model);

    @Mapping(target = "refreshToken", source = "refreshToken.token")
    @Mapping(target = "refreshTokenExpirationDate", source = "refreshToken.expiryDate")
    UserLoginResponse toLoginResponseDto(UserAuthProjection userAuthProjection);

    User toUserFromProjection(UserProjection userProjection);

    List<User> toModelList(List<UserLoginResponse> dtoList);
}
