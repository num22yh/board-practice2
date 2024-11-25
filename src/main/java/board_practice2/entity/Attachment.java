package board_practice2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="attachments")
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="original_name", nullable = false)
    private String originalName;

    @Column(name="stored_name", nullable = false)
    private String storedName;

    @Column(name="logical_path", nullable = false)
    private String logicalPath;

    @Column(name="physical_path", nullable = false)
    private String physicalPath;

    @Column(name="size", nullable = false)
    private long size;

    //TODO : 질문 - Attachment에서 Post를 참조하는 경우가 없다면 필요한지?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
