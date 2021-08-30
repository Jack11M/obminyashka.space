package space.obminyashka.items_exchange.dto;

public record RefreshTokenResponseDto(String accessToken, String refreshToken,
                                      String accessTokenExpiration, String refreshTokenExpiration) {
}
