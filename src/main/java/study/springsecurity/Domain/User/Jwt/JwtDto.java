package study.springsecurity.Domain.User.Jwt;

public record JwtDto(
        String accessToken,
        String refreshToken
) {}
