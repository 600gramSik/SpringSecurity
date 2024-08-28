package study.springsecurity.Domain.User.Dto.Response;

import lombok.Builder;
import study.springsecurity.Domain.User.Entity.User;

import java.time.LocalDateTime;

@Builder
public record UserSignupResponseDto(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt
) {
    public static UserSignupResponseDto from(User user) {
        return UserSignupResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getNickName())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
