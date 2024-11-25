package board_practice2.service;

import board_practice2.dto.CommentRequestDTO;
import board_practice2.dto.CommentResponseDTO;
import board_practice2.entity.Comment;
import board_practice2.entity.Post;
import board_practice2.repository.CommentRepository;
import board_practice2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addComment(CommentRequestDTO commentRequestDTO) {
        Post post = postRepository.findById(commentRequestDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ID: " + commentRequestDTO.getPostId()));

        Comment comment = new Comment();
        comment.setAuthor(commentRequestDTO.getAuthor());
        comment.setContent(commentRequestDTO.getContent());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        comment.setPost(post);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByPostId(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ID: " + postId));

        return commentRepository.findByPostOrderByCreatedAtDesc(post).stream()
                .map(comment -> {
                    CommentResponseDTO dto = new CommentResponseDTO();
                    dto.setId(comment.getId());
                    dto.setAuthor(comment.getAuthor());
                    dto.setContent(comment.getContent());
                    dto.setCreatedAt(comment.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
