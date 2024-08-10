package study.springsecurity.Domain.User.Dto.Response;

import lombok.Builder;
import study.springsecurity.Domain.User.Entity.User;

import java.time.LocalDateTime;

@Builder
public record UserLoginResponseDto(
        Long userId,
        LocalDateTime createdAt,
        String accessToken,
        String refreshToken
){
    public static UserLoginResponseDto from(User user, String accessToken, String refreshToken) {
        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
