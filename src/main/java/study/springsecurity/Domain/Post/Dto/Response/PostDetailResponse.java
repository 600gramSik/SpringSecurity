package study.springsecurity.Domain.Post.Dto.Response;

import lombok.Builder;
import study.springsecurity.Domain.Post.Entity.Post;

import java.time.LocalDateTime;

@Builder
public record PostDetailResponse(
        Long id,
        Long writerId,
        String writerNickName,
        String postTitle,
        String postContent,
        LocalDateTime createdAt
) {
    public static PostDetailResponse from(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .writerId(post.getUser().getId())
                .writerNickName(post.getUser().getNickName())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
