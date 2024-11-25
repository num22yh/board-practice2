package board_practice2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class PostSearchRequestDTO {
    private String startDate;
    private String endDate;
    private Integer categoryId;
    private String keyword;

    /**
     * 검색 조건이 있는지 판단하는 메서드
     *
     * @return true : 전달된 검색 조건이 있음.
     *         false : 전달된 검색 조건이 없음.
     */
    public boolean hasSearchCon(){
        return (startDate != null && !startDate.isEmpty()) ||
                (endDate != null && !endDate.isEmpty()) ||
                (categoryId != null && categoryId > 0) ||
                (keyword != null && !keyword.isEmpty());
    }
}
