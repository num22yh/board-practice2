package board_practice2.repository;

import board_practice2.entity.Comment;
import board_practice2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}
