package study.springsecurity.Domain.Post.Dto.Response;

import lombok.Builder;
import study.springsecurity.Domain.Post.Entity.Post;

import java.time.LocalDateTime;
@Builder
public record PostResponse(
        Long id,
        Long writerId,
        String writerNickName,
        String postTitle,
        String postContent,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .writerId(post.getUser().getId())
                .writerNickName(post.getUser().getNickName())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .createdAt(post.getCreatedAt())
                .build();
    }


}
