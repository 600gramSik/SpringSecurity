package study.springsecurity.Domain.User.Jwt.Dto;

public record JwtDto(
        String accessToken,
        String refreshToken
) {}
