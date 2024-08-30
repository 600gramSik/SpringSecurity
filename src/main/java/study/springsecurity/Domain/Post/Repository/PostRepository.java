package study.springsecurity.Domain.Post.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.springsecurity.Domain.Post.Entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
