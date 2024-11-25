package board_practice2.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class PostCreateRequestDTO {

    private String title;
    private String content;
    private String author;
    private String password;
    private int categoryId;
    private List<MultipartFile> attachments ;

}
