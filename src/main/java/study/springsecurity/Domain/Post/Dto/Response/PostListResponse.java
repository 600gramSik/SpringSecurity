package study.springsecurity.Domain.Post.Dto.Response;

import lombok.Builder;
import study.springsecurity.Domain.Post.Entity.Post;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record PostListResponse(
        Long id,
        Long writerId,
        String writerName,
        String postTitle
) {
    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .writerId(post.getUser().getId())
                .writerName(post.getUser().getNickName())
                .postTitle(post.getPostTitle())
                .build();
    }
    public static List<PostListResponse> from(List<Post> posts) {
        return posts.stream().map(PostListResponse::from).collect(Collectors.toList());
    }
}
