package board_practice2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    @Column(name="author", nullable = false, length = 5)
    private String author;

    @Column(name="password", nullable = false, length = 16)
    private String password;

    @Column(name="title", nullable = false, length = 100)
    private String title;

    @Column(name="content", nullable = false, length = 2000)
    private String content;

    @Column(name="view_count", nullable = false)
    private int viewCount;

    @Column(name="created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name="updated_at", nullable = false)
    private Timestamp updatedAt;

    // TODO : 질문 - 엔터티 관계들은 어떻게 설정해두는 것이 좋을까? 단방향 or 양방향?
    // fetch = FetchType.EAGER (즉시 로딩) vs FetchType.LAZY (지연 로딩)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
