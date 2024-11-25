package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PostListDTO {

    private String title;
    private String content;
    private int categoryId;
    private String author;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int viewCount;

    private boolean hasAttachments;
    private String categoryName;
}
