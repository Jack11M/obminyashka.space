package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.rest.exception.RefreshTokenException;
import space.obminyashka.items_exchange.rest.response.RefreshTokenResponse;
import space.obminyashka.items_exchange.rest.response.UserLoginResponse;

public interface AuthService {

    /**
     * Creates {@link UserLoginResponse} from {@param username} is user exists in the database
     *
     * @param userAuthDto added access token and refresh token to it
     * @return response on /login endpoint represented as {@link UserLoginResponse}
     */
    UserLoginResponse finalizeAuthData(UserLoginResponse userAuthDto);

    /**
     * Invalidate access and refresh tokens for selected user
     * @param accessToken non expired access token
     * @param username name of the user which the token belongs
     * @return result of the tokens invalidation
     */
    void logout(String accessToken, String username);

    /**
     * Create new access token using while refresh token is not expired
     * @param refreshToken token for re-creation access token
     * @return DTO containing tokens and their expiration time
     * @throws RefreshTokenException when token wasn't found
     */
    RefreshTokenResponse renewAccessTokenByRefresh(String refreshToken) throws RefreshTokenException;
}
