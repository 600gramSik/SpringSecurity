package study.springsecurity.Jwt.Dto;

public record JwtDto(
        String accessToken,
        String refreshToken
) {}
