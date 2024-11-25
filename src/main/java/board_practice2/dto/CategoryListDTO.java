package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryListDTO {

    private int id;
    private String categoryName;

    public CategoryListDTO(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
