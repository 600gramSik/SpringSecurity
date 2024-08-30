package study.springsecurity.Domain.Post.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import study.springsecurity.Domain.Post.Entity.Post;
import study.springsecurity.Domain.User.Entity.User;

public record PostRequest(
        @NotBlank(message = "게시글 제목은 필수 입력입니다.")
        String postTitle,
        @NotBlank(message = "게시글 내용은 필수 입력입니다.")
        String postContent
) {
    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .postTitle(postTitle)
                .postContent(postContent)
                .build();
    }
}
