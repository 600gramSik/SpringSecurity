package study.springsecurity.Domain.Post.Entity;

import jakarta.persistence.*;
import lombok.*;
import study.springsecurity.Domain.Post.Dto.Request.PostRequest;
import study.springsecurity.Domain.User.Entity.User;
import study.springsecurity.Global.Common.BaseEntity;

@Builder
@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @Column(name = "post_content", nullable = false)
    private String postContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updatePost(PostRequest request){
        postTitle = request.postTitle();
        postContent = request.postContent();
    }

}
