package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class CommentResponseDTO {
    private int id;
    private String author;
    private String content;
    private Timestamp createdAt;
}
