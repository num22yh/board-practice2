package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;

@Getter @Setter
public class PostDetailDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String categoryName;
    private int viewCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<String> attachments;
}
