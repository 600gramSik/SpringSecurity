package study.springsecurity.Domain.Post.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springsecurity.Domain.Post.Dto.Request.PostRequest;
import study.springsecurity.Domain.Post.Dto.Response.PostResponse;
import study.springsecurity.Domain.Post.Entity.Post;
import study.springsecurity.Domain.Post.Exception.PostExceptionHandler;
import study.springsecurity.Domain.Post.Repository.PostRepository;
import study.springsecurity.Domain.User.Entity.User;
import study.springsecurity.Domain.User.Exception.UserExceptionHandler;
import study.springsecurity.Domain.User.Repository.UserJpaRepository;
import study.springsecurity.Global.Common.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final UserJpaRepository userJpaRepository;
    private final PostRepository postRepository;

    public PostResponse createPost(String email, PostRequest request) {
        User user = userJpaRepository.findByEmail(email).orElseThrow(() -> new UserExceptionHandler(ErrorCode.USER_NOT_FOUND));
        Post post = request.toEntity(user);
        postRepository.save(post);
        return PostResponse.from(post);
    }

    public PostResponse updatePost(String email, Long postId, PostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostExceptionHandler(ErrorCode.POST_NOT_FOUND));
        if(!post.getUser().getEmail().equals(email)) {
            throw new PostExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }
        post.updatePost(request);
        return PostResponse.from(post);
    }

    public void deletePost(String email, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostExceptionHandler(ErrorCode.POST_NOT_FOUND));
        if(!post.getUser().getEmail().equals(email)) {
            throw new PostExceptionHandler(ErrorCode.NO_AUTHORIZATION);
        }
        postRepository.delete(post);
    }
}
