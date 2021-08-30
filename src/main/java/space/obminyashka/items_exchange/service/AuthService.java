package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dto.UserLoginResponseDto;

public interface AuthService {

    /**
     * Creates {@link UserLoginResponseDto} from {@param username} is user exists in the database
     *
     * @param username user for children update
     * @return response on /login endpoint represented as {@link UserLoginResponseDto}
     */
    UserLoginResponseDto createUserLoginResponseDto(String username);
}
