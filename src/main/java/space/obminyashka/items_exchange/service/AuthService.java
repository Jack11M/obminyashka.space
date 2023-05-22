package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dto.RefreshTokenResponseDto;
import space.obminyashka.items_exchange.dto.UserLoginResponseDto;
import space.obminyashka.items_exchange.exception.RefreshTokenException;

public interface AuthService {

    /**
     * Creates {@link UserLoginResponseDto} from {@param username} is user exists in the database
     *
     * @param username user for children update
     * @return response on /login endpoint represented as {@link UserLoginResponseDto}
     */
    UserLoginResponseDto createUserLoginResponseDto(String username);

    /**
     * Invalidate access and refresh tokens for selected user
     * @param accessToken non expired access token
     * @param username name of the user which the token belongs
     * @return result of the tokens invalidation
     */
    boolean logout(String accessToken, String username);

    /**
     * Create new access token using while refresh token is not expired
     * @param refreshToken token for re-creation access token
     * @return DTO containing tokens and their expiration time
     * @throws RefreshTokenException when token wasn't found
     */
    RefreshTokenResponseDto renewAccessTokenByRefresh(String refreshToken) throws RefreshTokenException;
}
