package study.springsecurity.Domain.Post.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.springsecurity.Domain.Post.Dto.Request.PostRequest;
import study.springsecurity.Domain.Post.Dto.Response.PostDetailResponse;
import study.springsecurity.Domain.Post.Dto.Response.PostListResponse;
import study.springsecurity.Domain.Post.Dto.Response.PostResponse;
import study.springsecurity.Domain.Post.Service.PostQueryService;
import study.springsecurity.Domain.Post.Service.PostService;
import study.springsecurity.Domain.User.Jwt.UserDetail.CustomUserDetail;
import study.springsecurity.Global.Common.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostQueryService postQueryService;
    @GetMapping
    public ApiResponse<Page<PostListResponse>> getPostList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostListResponse> postList = postQueryService.getAllPosts(pageable);
        return ApiResponse.onSuccess(postList);
    }


    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPost(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                   @PathVariable Long postId) {
        return ApiResponse.onSuccess(postQueryService.getPost(postId));
    }


    @PostMapping
    public ApiResponse<PostResponse> createPost(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                @RequestBody PostRequest request) {
        PostResponse responseDto = postService.createPost(customUserDetail.getUsername(), request);
        return ApiResponse.onSuccess(responseDto);
    }


    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                @PathVariable Long postId,
                                                @RequestBody PostRequest request) {
        PostResponse responseDto = postService.updatePost(customUserDetail.getUsername(), postId, request);
        return ApiResponse.onSuccess(responseDto);
    }


    @DeleteMapping("/{postId}")
    public ApiResponse<Object> deletePost(@AuthenticationPrincipal CustomUserDetail customUserDetail, @PathVariable Long postId) {
        postService.deletePost(customUserDetail.getUsername(), postId);
        return ApiResponse.noContent();
    }

}
