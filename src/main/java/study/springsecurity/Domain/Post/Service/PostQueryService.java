package study.springsecurity.Domain.Post.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springsecurity.Domain.Post.Dto.Response.PostDetailResponse;
import study.springsecurity.Domain.Post.Dto.Response.PostListResponse;
import study.springsecurity.Domain.Post.Entity.Post;
import study.springsecurity.Domain.Post.Exception.PostExceptionHandler;
import study.springsecurity.Domain.Post.Repository.PostRepository;
import study.springsecurity.Domain.User.Entity.User;
import study.springsecurity.Global.Common.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {
    private final PostRepository postRepository;

    //전체 게시물 조회
    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostListResponse> postListResponses = PostListResponse.from(posts.getContent());
        return new PageImpl<>(postListResponses, pageable, posts.getTotalElements());
        /**
         * 	postListResponses: 현재 페이지에 해당하는 DTO 리스트(실제 데이터).
         * 	pageable: 페이지 요청 정보(어떤 페이지를 요청했는지, 페이지 크기, 정렬 방식 등).
         * 	posts.getTotalElements(): 전체 데이터 개수(페이징되지 않은 전체 데이터셋의 크기).
         */
    }

    public PostDetailResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostExceptionHandler(ErrorCode.POST_NOT_FOUND));
        return PostDetailResponse.from(post);
    }



}
