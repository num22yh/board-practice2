package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequestDTO {
    private String author;
    private String content;
    private Long postId;
}
