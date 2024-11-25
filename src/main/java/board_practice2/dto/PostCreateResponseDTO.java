package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class PostCreateResponseDTO {

    private String title;
    private String content;
    private int categoryId;
    private String author;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int viewCount = 0;


    public PostCreateResponseDTO(String title, String content, int categoryId, String author, Timestamp createdAt, Timestamp updatedAt, int viewCount) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
    }
}
